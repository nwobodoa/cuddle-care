package com.ebony.cuddlecare.ui.auth

import Profile
import android.content.ContentValues
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

data class UserAuthUIState(
    val user: Profile? = null,
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
    private val db = Firebase.firestore

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


    fun setUser(user: Profile?) {
        _userAuthUIState.update { it.copy(user = user) }
        persistProfile()
    }

    fun saveProfile(uuid: String, user: Profile): Task<Void> {
        val profileProfileRef = db.collection(Document.Profile.name).document(uuid)
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
            db.collection(Document.Profile.name)
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    setUser(it.toObject(Profile::class.java))
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

}
