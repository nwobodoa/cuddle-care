package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Document
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Profile(
    val uuid: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname:String = "",
    val primaryCareGiverTo:List<String> = listOf(),
    val careGiverTo: List<String> = listOf(),
    val activeBabyId:String? = null

) {
    constructor() : this("","","","",listOf(), listOf())
    fun hasAtLeastABabyInCare() = primaryCareGiverTo.isNotEmpty() || careGiverTo.isNotEmpty()
}

data class UserUIState(
    val uid: String = "",
    val user: Profile? = null,
    val loading: Boolean = true,
    val isSaved: Boolean = false,
)

class ProfileViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _userUISate = MutableStateFlow(UserUIState())
    val userUIState = _userUISate.asStateFlow()

    private fun setLoading(loading:Boolean) {
        _userUISate.update { it.copy(loading = loading) }
    }

    fun setUser(user: Profile?) {
        _userUISate.update { it.copy(user = user) }
    }

    fun saveProfile(uuid: String, user: Profile): Task<Void> {
        val profileProfileRef = db.collection(Document.Profile.name).document(uuid)
        return profileProfileRef.set(user.copy(uuid = uuid))
    }

    fun persistProfile() {
        _userUISate.value.user?.let { saveProfile(_userUISate.value.user!!.uuid, it) }
    }

    fun setActiveBaby(babyId:String) {
        _userUISate.update { it.copy(user = it.user?.copy( activeBabyId = babyId)) }
        persistProfile()
    }

    fun loadUser(user: FirebaseUser?) {
        if (user != null) {
            setLoading(true)
            db.collection(Document.Profile.name)
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    setUser(it.toObject(Profile::class.java))
                }
                .addOnCompleteListener{
                    setLoading(false)
                }
                .addOnFailureListener{
                    Log.e(ContentValues.TAG, "loadUser: ${it.message}", )
                }
            return
        }
        setLoading(false)

    }
}
