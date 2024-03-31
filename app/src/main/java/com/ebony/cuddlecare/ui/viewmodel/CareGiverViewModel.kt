package com.ebony.cuddlecare.ui.viewmodel

import com.ebony.cuddlecare.ui.documents.CareGiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.Invite
import com.ebony.cuddlecare.ui.documents.InviteStatus
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.checkerframework.framework.qual.LiteralKind
import java.util.Locale

data class CareGiverUIState(
    val baby: Baby? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val inviteResponse: String? = null,
    val pendingSentInvites: List<Invite> = emptyList()
)


class CareGiverViewModel : ViewModel() {
    private val _careGiverUIState = MutableStateFlow(CareGiverUIState())
    val careGiverUIState = _careGiverUIState.asStateFlow()
    private val db = Firebase.firestore
    private val inviteCollection = db.collection(Document.Invite.name)


    fun setBaby(baby: Baby) {
        _careGiverUIState.update { it.copy(baby = baby) }
    }

    fun resetResponse() {
        _careGiverUIState.update { it.copy(inviteResponse = null) }
    }

    fun removeCareGiverFromBaby(primaryCareGiverId: String, careGiverToRemove: CareGiver) {
        val baby = _careGiverUIState.value.baby
        val isPrimaryCareGiver =
            baby?.primaryCareGiverIds?.any { it == primaryCareGiverId } ?: false


        if (!isPrimaryCareGiver) {
            _careGiverUIState.update { it.copy(error = "Cannot update baby as you are not a primary careGiver") }
            return
        }

        if (baby == null) {
//            TODO: Handle when baby is null
            return
        }


        val updatedBaby =
            baby.copy(careGiverIds = baby.careGiverIds.filter { id -> id != careGiverToRemove.uuid })


        val updateCareGiverToRemove =
            careGiverToRemove.copy(careGiverTo = careGiverToRemove.careGiverTo.filterNot { it == baby.id })

        val babyRef = db.collection(Document.Baby.name).document(baby.id)
        val profileRef = db.collection(Document.Profile.name).document(updateCareGiverToRemove.uuid)


        db.runTransaction { tx ->
            tx.set(babyRef, updatedBaby)
            tx.set(profileRef, updateCareGiverToRemove)
        }.addOnSuccessListener {
            Log.d(TAG, "createBaby: Successful")
        }.addOnFailureListener { e ->
            Log.e(TAG, "createBaby: ", e)
        }

    }

    fun setLoading(loading: Boolean) {
        _careGiverUIState.update { it.copy(loading = loading) }
    }

    fun setInviteSendResponse(response: String?) {
        _careGiverUIState.update { it.copy(inviteResponse = response) }
    }


    fun sendInvite(careGiverEmail: String, currentUserId: String) {
        if (_careGiverUIState.value.baby?.primaryCareGiverIds?.contains(currentUserId) != true) {
            // TODO handle with appropriate message (unauthorized)
            return
        }
        val uniqueKey = "${careGiverEmail.lowercase()}${_careGiverUIState.value.baby!!.id}"
        val inviteRef = inviteCollection.document(uniqueKey)

        _careGiverUIState.value.baby?.let {
            val inviteToSave = Invite(
                id = inviteRef.id,
                careGiverEmail = careGiverEmail,
                primaryCareGiverId = currentUserId,
                babyId = it.id,
                babyName = it.name
            )

            setLoading(true)
            // TODO Handle duplicate invites
            inviteRef.set(inviteToSave)
                .addOnFailureListener { e ->
                    setInviteSendResponse(e.message)
                    Log.e(TAG, "sendInvite: ", e)
                }
                .addOnSuccessListener {
                    setInviteSendResponse("Invite Sent!!")
                }
                .addOnCompleteListener {
                    setInviteSendResponse("Finally Sent!!")
                    setLoading(false)
                }
        }

    }


    fun fetchPendingSentInvites(babyId: String, currentUserId: String) {
        inviteCollection
            .whereEqualTo("babyId", babyId)
            .whereEqualTo("primaryCareGiverId", currentUserId)
            .addSnapshotListener { snap, ex ->
                if (ex != null) {
                    //TODO handle exception gracefully and tell user of the problem
                    return@addSnapshotListener
                }
                val pendingSentInvites =
                    snap?.documents?.mapNotNull { it.toObject(Invite::class.java) } ?: emptyList()
                _careGiverUIState.update { it.copy(pendingSentInvites = pendingSentInvites) }
            }
    }
}