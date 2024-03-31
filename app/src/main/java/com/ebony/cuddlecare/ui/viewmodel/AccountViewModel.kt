package com.ebony.cuddlecare.ui.viewmodel


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.auth.UserAuthViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.CareGiver
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.Invite
import com.ebony.cuddlecare.ui.documents.InviteStatus
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccountUIState(
    val pendingInvites: List<Invite> = emptyList(),
    val loading: Boolean = false,
    val isAccountManagementOpen: Boolean = false
)

class AccountViewModel(
    private val babyViewModel: BabyViewModel = BabyViewModel(),
    private val userAuthViewModel: UserAuthViewModel = UserAuthViewModel()
) : ViewModel() {
    private val db = Firebase.firestore
    private val inviteCollection = db.collection(Document.Invite.name)
    private val _accountUIState = MutableStateFlow(AccountUIState())
    val accountUIState = _accountUIState.asStateFlow()

    fun setLoading(loading: Boolean) {
        _accountUIState.update { it.copy(loading = loading) }
    }

    fun setPendingInvites(pendingInvites: List<Invite>) {
        _accountUIState.update { it.copy(pendingInvites = pendingInvites) }
    }

    fun setIsAccountManagementOpen(state: Boolean) {
        _accountUIState.update { it.copy(isAccountManagementOpen = state) }
    }

    fun checkInvites(currentUser: CareGiver) {
        inviteCollection
            .whereEqualTo("careGiverEmail", currentUser.email)
            .whereEqualTo("status", InviteStatus.PENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    setLoading(false)
                    return@addSnapshotListener
                }
                val pendInvites =
                    snapshot?.documents?.mapNotNull { it.toObject(Invite::class.java) }
                        ?: emptyList()
                setPendingInvites(pendInvites)
            }
    }

    fun processInvite(currentUser: CareGiver, invite: Invite) {
        if (invite.status == InviteStatus.ACCEPTED) {
            acceptInvite(invite, currentUser)
        }
//        TODO: Handle reject invite
    }

    private fun acceptInvite(
        invite: Invite,
        currentUser: CareGiver
    ) {
        babyViewModel.babyById(invite.babyId)
            .addOnSuccessListener {
                val baby = it.toObject(Baby::class.java)
                baby?.let {
                    val updatedBaby = baby.copy(careGiverIds = baby.careGiverIds + currentUser.uuid)
                    val updatedUser =
                        currentUser.copy(careGiverTo = currentUser.careGiverTo + baby.id)


                    val profileRef = userAuthViewModel.profileCollection.document(updatedUser.uuid)
                    val babyRef = babyViewModel.babyCollectionRef.document(baby.id)
                    val inviteRef = inviteCollection.document(invite.id)

                    db.runTransaction { tx ->
                        tx.set(babyRef, updatedBaby)
                        tx.set(profileRef, updatedUser)
                        tx.set(inviteRef, invite)
                    }.addOnSuccessListener {
                        Log.d(TAG, "processed invite for caregiver successfully")
                    }.addOnFailureListener { e ->
                        Log.e(TAG, "failed try to process invite ", e)
                    }
                }
            }.addOnFailureListener {
                Log.e(TAG, "processInvite: An error occurred fetching baby ", it)
            }
    }


}