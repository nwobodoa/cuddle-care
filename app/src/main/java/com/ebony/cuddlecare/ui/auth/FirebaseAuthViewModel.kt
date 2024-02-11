package com.ebony.cuddlecare.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FirebaseAuthUiState(
    val currentUser: FirebaseUser? = null,
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: List<String> = emptyList(),
    val isRegistrationSuccessful: Boolean = false
)
class FirebaseAuthViewModel : ViewModel(){
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _firebaseAuthUIState = MutableStateFlow(FirebaseAuthUiState())
    val firebaseAuthUiState = _firebaseAuthUIState.asStateFlow()

    init {
        setCurrentUser(firebaseAuth.currentUser)
    }

    private fun clearErrors() {
        _firebaseAuthUIState.update { it.copy(errors = emptyList()) }
    }
    private fun updateRegistrationState(state: Boolean) {
        _firebaseAuthUIState.update { it.copy(isRegistrationSuccessful = state) }
    }
    private fun isValidEmail(email: String): Boolean {
        return email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun setEmail(email: String) {
        _firebaseAuthUIState.update { it.copy(email = email.trim()) }
    }

    private fun setCurrentUser(user: FirebaseUser? = null) {
        _firebaseAuthUIState.update { it.copy(currentUser = user) }
    }

       fun currentUser(): FirebaseUser?{
        return (_firebaseAuthUIState.value.currentUser ?: firebaseAuth.currentUser)
    }
    fun isAuthenticated(): Boolean{
        return _firebaseAuthUIState.value.currentUser != null
    }
     fun signInWithEmailAndPassword(){
         setLoadingState(true)
         firebaseAuth . signInWithEmailAndPassword(
             _firebaseAuthUIState.value.email.trim(),
             _firebaseAuthUIState.value.password.trim()
         ).addOnSuccessListener{
             setCurrentUser((it.user))
         }.addOnFailureListener{
             addError("Invalid email or password")
         }.addOnCompleteListener{
             setLoadingState(false)
         }
     }
    fun createAccount() {
        if (isValidated()) {
            setLoadingState(true)
            firebaseAuth.createUserWithEmailAndPassword(
                _firebaseAuthUIState.value.email.trim(),
                _firebaseAuthUIState.value.password.trim()
            ).addOnSuccessListener {
                signOut()
                updateRegistrationState(true)
            }.addOnFailureListener { failure ->
                addError(failure.message.toString())
            }.addOnCompleteListener {
                setLoadingState(false)
            }

        }
    }
    private fun isValidated(): Boolean {
        clearErrors()
        val mPassword = _firebaseAuthUIState.value.password.trim()
        val mEmail = _firebaseAuthUIState.value.email.trim()
        val mConfirmPassword = _firebaseAuthUIState.value.confirmPassword.trim()

        if (mConfirmPassword != mPassword) {
            addError("Password and Confirmation do not match")
        }
        if (mConfirmPassword == "") {
            addError("Password cannot be empty")
        }
        if (mEmail.isEmpty()) {
            addError("Email cannot be empty")
        }

        if (!isValidEmail(mEmail)) {
            addError("Email is not valid")
        }
        return _firebaseAuthUIState.value.errors.isEmpty()
    }

    private fun addError(err: String) {
        _firebaseAuthUIState.update { it.copy(errors = it.errors.plus(err)) }
    }

    private fun setLoadingState(state: Boolean) {
        _firebaseAuthUIState.update { it.copy(loading = state) }
    }
    fun signOut() {
        firebaseAuth.signOut()
        setCurrentUser()
    }

    fun setPassword(password: String) {
        _firebaseAuthUIState.update { it.copy(password = password) }
    }

    fun setConfirmPassword(confirmPassword: String) {
        _firebaseAuthUIState.update { it.copy(confirmPassword = confirmPassword) }
    }
}