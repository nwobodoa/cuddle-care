package com.ebony.cuddlecare.ui.auth

import CareGiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Document
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

class UserAuthViewModel() : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _userAuthUIState = MutableStateFlow(UserAuthUIState())
    val userAuthUIState = _userAuthUIState.asStateFlow()
    private val profileCollection = Firebase.firestore.collection(Document.Profile.name)

    init {
        firebaseAuth.addAuthStateListener {
            if (it.currentUser == null) {
                setUser(null)
            }
        }

    }

    private fun setLoading(loading: Boolean) {
        _userAuthUIState.update { it.copy(loading = loading) }
    }


    fun reset() {
        _userAuthUIState.value = UserAuthUIState()
    }

    fun isAuthenticated(): Boolean {
        return _userAuthUIState.value.user != null
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
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    setUser(it.toObject(CareGiver::class.java))
                }
                .addOnCompleteListener {
                    setLoading(false)
                }
                .addOnFailureListener {
                    Log.e(ContentValues.TAG, "loadUser: ${it.message}")
                }
            return
        }
        setLoading(false)

    }

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }


    suspend fun loadUsers(userIds: List<String>): List<CareGiver> {
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
