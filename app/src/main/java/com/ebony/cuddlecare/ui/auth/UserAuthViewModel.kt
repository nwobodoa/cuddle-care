package com.ebony.cuddlecare.ui.auth

import com.ebony.cuddlecare.ui.documents.CareGiver
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.Invite
import com.ebony.cuddlecare.ui.documents.InviteStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

data class UserAuthUIState(
    val user: CareGiver? = null,
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false
)

class UserAuthViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _userAuthUIState = MutableStateFlow(UserAuthUIState())
    val userAuthUIState = _userAuthUIState.asStateFlow()
    val profileCollection = Firebase.firestore.collection(Document.Profile.name)

    init {
        firebaseAuth.addAuthStateListener {
            if (it.currentUser == null) {
                setUser(null)
            } else {
                loadUser(it.currentUser)
            }
        }

    }

    private fun setLoading(loading: Boolean) {
        _userAuthUIState.update { it.copy(loading = loading) }
    }


    private fun reset() {
        _userAuthUIState.update { UserAuthUIState() }
    }


    fun signInWithEmailAndPassword() {
        setLoading(true)
        firebaseAuth.signInWithEmailAndPassword(
            _userAuthUIState.value.email.trim(),
            _userAuthUIState.value.password.trim()
        ).addOnSuccessListener {
            loadUser(it.user)
        }.addOnFailureListener {
            _userAuthUIState.update { it.copy(error = "Invalid email or password") }

        }.addOnCompleteListener {
            setLoading(false)
        }
    }


    fun signOut() {
        firebaseAuth.signOut()
        reset()
    }

    fun setEmail(email: String) {
        _userAuthUIState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) {
        _userAuthUIState.update { it.copy(password = password) }
    }

    fun setUser(user: CareGiver?) {
        _userAuthUIState.update { it.copy(user = user) }
        persistProfile()
    }

    fun saveProfile(uuid: String, user: CareGiver): Task<Void> {
        val profileProfileRef = profileCollection.document(uuid)
        return profileProfileRef.set(user.copy(uuid = uuid))
    }

    private fun persistProfile() {
        _userAuthUIState.value.user?.let {
            saveProfile(
                _userAuthUIState.value.user!!.uuid,
                it
            )
        }
    }

    fun setActiveBaby(babyId: String) {
        _userAuthUIState.update { it.copy(user = it.user?.copy(activeBabyId = babyId)) }
        persistProfile()
    }

    private fun loadUser(user: FirebaseUser?) {
        if (user != null) {
            setLoading(true)
            profileCollection
                .whereEqualTo("uuid", user.uid)
                .addSnapshotListener { snap, ex ->
                    setLoading(false)
                    if (ex != null) {
                        Log.e(TAG, "loadUser: error loading user", ex)
                        return@addSnapshotListener
                    }
                    snap?.documents?.firstNotNullOfOrNull { it.toObject(CareGiver::class.java) }
                        ?.let {
                            setUser(it)
                        }
                }
            return
        }
        setLoading(false)

    }

    suspend fun loadUsers(userIds: List<String>): List<CareGiver> {
        if (userIds.isEmpty()) return emptyList()
        return try {
            val querySnapshot = profileCollection
                .whereIn("uuid", userIds).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(CareGiver::class.java) }

        } catch (e: Exception) {
            Log.e(TAG, "loadUsers: ", e)
            emptyList()
        }
    }


}
