package com.ebony.cuddlecare.ui.documents

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.content.ContentValues.TAG
import androidx.lifecycle.viewmodel.compose.viewModel


data class UserProfile(
    val uuid: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname:String = "",
    val primaryCareGiverTo:List<String> = listOf(),
    val careGiverTo: List<String> = listOf(),

    ) {
    constructor() : this("","","","",listOf(), listOf())
    fun hasAtLeastABabyInCare() = primaryCareGiverTo.isNotEmpty() || careGiverTo.isNotEmpty()
}

data class UserUIState(
    val uid: String = "",
    val user: UserProfile? = null,
    val loading: Boolean = true,
    val isSaved: Boolean = false,
)

class UserViewModel(): ViewModel() {
    private val db = Firebase.firestore
    private val _userUISate = MutableStateFlow(UserUIState())
    val userUIState = _userUISate.asStateFlow()

    private fun setLoading(loading:Boolean) {
        _userUISate.update { it.copy(loading = loading) }
    }

    private fun setUser(user:UserProfile?) {
        _userUISate.update { it.copy(user = user) }
    }
    fun loadUser(user: FirebaseUser?) {
        if (user != null) {
            setLoading(true)
            db.collection(Document.User.name)
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    setUser(it.toObject(UserProfile::class.java))
                }
                .addOnCompleteListener{
                    setLoading(false)
                }
                .addOnFailureListener{
                    Log.e(TAG, "loadUser: ${it.message}", )
                }
            return
        }
        setLoading(false)

    }
}
