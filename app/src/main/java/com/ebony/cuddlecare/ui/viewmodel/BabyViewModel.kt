package com.ebony.cuddlecare.ui.viewmodel

import CareGiver
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebony.cuddlecare.ui.auth.UserAuthViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.Gender
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class BabyUIState(
    val addBabyUIFormState: AddBabyUIFormState = AddBabyUIFormState(),
    val activeBaby: Baby? = null,
    val listOfBabies: List<Baby> = emptyList(),
    val loading: Boolean = false
)

data class AddBabyUIFormState(
    val selectedGender: Gender = Gender.BOY,
    val babyName: String = "",
    val isPremature: Boolean = false,
    val selectedDate: Long = LocalTime.now().atDate(LocalDate.now()).atZone(ZoneId.systemDefault())
        .toInstant().toEpochMilli(),
    val showDatePicker: Boolean = false,
    val isSaved: Boolean = false,
    val isDateExpanded: Boolean = false,
    val activeBaby: Baby? = null,
)

class BabyViewModel : ViewModel() {
    private val _babyUIState: MutableStateFlow<BabyUIState> = MutableStateFlow(BabyUIState())
    val babyUIState: StateFlow<BabyUIState> = _babyUIState.asStateFlow()
    private val db = Firebase.firestore
    private val babyCollectionRef = db.collection(Document.Baby.name)
    private val userAuthViewModel = UserAuthViewModel()

    fun setSelectedGender(gender: Gender) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(selectedGender = gender)) }
    }

    fun setIsSaved(state: Boolean) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(isSaved = state)) }
    }

    fun setIsPremature(state: Boolean) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(isPremature = state)) }
    }

    fun setBabyName(name: String) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(babyName = name)) }
    }

    fun showDatePicker(isVisible: Boolean) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(showDatePicker = isVisible)) }
    }

    fun setSelectedDate(l: Long) {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(selectedDate = l)) }
    }

    fun setLoading(loading: Boolean) {
        _babyUIState.update { it.copy(loading = loading) }
    }

    fun toggableDatePicker() {
        _babyUIState.update { it.copy(addBabyUIFormState = it.addBabyUIFormState.copy(isDateExpanded = !it.addBabyUIFormState.isDateExpanded)) }
    }

    fun createBaby(user: CareGiver, setUpdatedUser: (CareGiver) -> Unit) {
        val babyRef = babyCollectionRef.document()
        val baby = Baby(
            id = babyRef.id,
            name = _babyUIState.value.addBabyUIFormState.babyName,
            dateOfBirth = _babyUIState.value.addBabyUIFormState.selectedDate,
            gender = _babyUIState.value.addBabyUIFormState.selectedGender,
            isPremature = _babyUIState.value.addBabyUIFormState.isPremature,
            primaryCareGiverIds = listOf(user.uuid)

        )
        val updatedUser = user.copy(
            primaryCareGiverTo = user.primaryCareGiverTo + baby.id, activeBabyId = babyRef.id
        )

        Log.d(TAG, "createBaby: ${user.uuid}")
        val profileRef = db.collection(Document.Profile.name).document(user.uuid)

        db.runTransaction { tx ->
            tx.set(babyRef, baby)
            tx.set(profileRef, updatedUser)
        }.addOnSuccessListener {
            setUpdatedUser(updatedUser)
            Log.d(TAG, "createBaby: Successful")
            setIsSaved(true)
        }.addOnFailureListener { e ->
            Log.e(TAG, "createBaby: ", e)
            setIsSaved(false)
        }

    }

    private suspend fun updateCareGivers(baby: Baby): Baby {
        val careGivers = userAuthViewModel.loadUsers(baby.careGiverIds)
        val primaryCareGivers = userAuthViewModel.loadUsers(baby.primaryCareGiverIds)
        return baby.copy(careGivers = careGivers, primaryCareGivers = primaryCareGivers)
    }

    fun fetchBabies(user: CareGiver) {
        val babyIds = user.careGiverTo + user.primaryCareGiverTo
        if (babyIds.isEmpty()) {
            return
        }
        setLoading(true)
        babyCollectionRef.whereIn("id", babyIds).addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                setLoading(false)
                return@addSnapshotListener
            }
            val babies = querySnapshot?.documents?.mapNotNull { it.toObject(Baby::class.java) }
//                ensure that the user has the right to see baby
            updateUIWithBabies(babies, user)

        }
    }

    private fun updateUIWithBabies(
        babies: List<Baby>?,
        user: CareGiver
    ) {
        viewModelScope.launch {
            babies?.let {
                val babiesWithCareGivers = babies.map { updateCareGivers(it) }
                val activeBaby = babiesWithCareGivers.firstOrNull { it.id == user.activeBabyId }
                _babyUIState.update {
                    it.copy(
                        listOfBabies = babiesWithCareGivers,
                        activeBaby = activeBaby
                    )
                }
                setLoading(false)
            }
        }
    }
}
