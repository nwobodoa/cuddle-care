package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.components.epochMillisToDate
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.Gender
import com.ebony.cuddlecare.ui.documents.UserProfile
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class AddBabyUIState(
    val selectedGender: Gender = Gender.BOY,
    val babyName: String = "",
    val isPremature: Boolean = false,
    val selectedDate: Long = 0L,
    val showDatePicker: Boolean = false,
    val isSaved: Boolean = false,
    val isDateExpanded: Boolean = false
)

class AddBabyViewModel : ViewModel() {
    private val _addBabyUIState: MutableStateFlow<AddBabyUIState> =
        MutableStateFlow(AddBabyUIState())
    val addBabyUIState: StateFlow<AddBabyUIState> = _addBabyUIState.asStateFlow()
    private val db = Firebase.firestore

    fun setSelectedGender(gender: Gender) {
        _addBabyUIState.update { it.copy(selectedGender = gender) }
    }

    fun setIsSaved(state: Boolean) {
        _addBabyUIState.update { it.copy(isSaved = state) }
    }

    fun setBabyName(name: String) {
        _addBabyUIState.update { it.copy(babyName = name) }
    }

    fun showDatePicker(isVisible: Boolean) {
        _addBabyUIState.update { it.copy(showDatePicker = isVisible) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(l: Long) {
        _addBabyUIState.update { it.copy(selectedDate = l) }
    }

    fun toggableDatePicker() {
        _addBabyUIState.update { it.copy(isDateExpanded = !it.isDateExpanded) }
    }

    fun createBaby(user: UserProfile) {
        val babyRef = db.collection(Document.Baby.name).document()
        val baby = Baby(
            id = babyRef.id,
            name = _addBabyUIState.value.babyName,
            dateOfBirth = _addBabyUIState.value.selectedDate,
            gender = _addBabyUIState.value.selectedGender,
            isPremature = _addBabyUIState.value.isPremature,
            primaryCareGivers = listOf(user.uuid)

        )
        val updateUser = user.copy(primaryCareGiverTo = user.primaryCareGiverTo + baby.id)

        Log.d(TAG, "createBaby: ${user.uuid}")
        val userRef = db.collection(Document.User.name).document(user.uuid)

        db.runTransaction { tx ->
            tx.set(babyRef, baby)
            tx.set(userRef, updateUser)
        }.addOnSuccessListener {
            Log.d(TAG, "createBaby: Successful")
            setIsSaved(true)
        }.addOnFailureListener { e ->
            Log.e(TAG, "createBaby: ", e)
            setIsSaved(false)
        }
    }
}
