package com.ebony.cuddlecare.ui.auth

import com.ebony.cuddlecare.ui.documents.CareGiver
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUIState(
    val loading: Boolean = false,
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: List<String> = emptyList(),
    val isRegistrationSuccessful: Boolean = false
)
class RegisterViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _registerUIState = MutableStateFlow(RegisterUIState())
    val registerUIState = _registerUIState.asStateFlow()
    private val userAuthViewModel = UserAuthViewModel()

    fun setFirstname(firstname: String) {
        _registerUIState.update { it.copy(firstname = firstname) }
    }

    fun setLastname(lastname: String) {
        _registerUIState.update { it.copy(lastname = lastname) }
    }

    private fun clearErrors() {
        _registerUIState.update { it.copy(errors = emptyList()) }
    }

    private fun updateRegistrationState(state: Boolean) {
        _registerUIState.update { it.copy(isRegistrationSuccessful = state) }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun setEmail(email: String) {
        _registerUIState.update { it.copy(email = email.trim()) }
    }

    fun setPassword(password: String) {
        _registerUIState.update { it.copy(password = password) }
    }

    fun createAccount() {
        if (isValidated()) {
            setLoading(true)
            firebaseAuth.createUserWithEmailAndPassword(
                _registerUIState.value.email.trim(),
                _registerUIState.value.password.trim()
            ).addOnSuccessListener {
                val user = CareGiver(
                    uuid = it.user!!.uid,
                    email = _registerUIState.value.email,
                    firstname = _registerUIState.value.firstname,
                    lastname = _registerUIState.value.lastname
                )

                userAuthViewModel.saveProfile(it.user!!.uid, user)
                    .addOnFailureListener { e ->
                        addError(e.message.toString())
                        it.user!!.delete()
                    }.addOnSuccessListener {
                        updateRegistrationState(true)
                    }.addOnCompleteListener {
                        setLoading(false)
                    }

            }.addOnFailureListener { failure ->
                addError(failure.message.toString())
            }

        }
    }


    private fun isValidated(): Boolean {
        clearErrors()
        val mPassword = _registerUIState.value.password.trim()
        val mEmail = _registerUIState.value.email.trim()
        val mConfirmPassword = _registerUIState.value.confirmPassword.trim()
        val firstname = _registerUIState.value.firstname.trim()
        val lastname = _registerUIState.value.lastname.trim()

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

        if (firstname.isBlank()) {
            addError("Firstname cannot be empty")
        }

        if (lastname.isBlank()) {
            addError("Lastname cannot be empty")
        }

        return _registerUIState.value.errors.isEmpty()
    }

    private fun addError(err: String) {
        _registerUIState.update { it.copy(errors = it.errors.plus(err)) }
    }

    private fun setLoading(state: Boolean) {
        _registerUIState.update { it.copy(loading = state) }
    }
    fun setConfirmPassword(confirmPassword: String) {
        _registerUIState.update { it.copy(confirmPassword = confirmPassword) }
    }

    
}