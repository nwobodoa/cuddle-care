package com.ebony.cuddlecare.ui.documents

import CareGiver
import com.google.firebase.firestore.Exclude

enum class Gender{
    BOY,
    GIRL
}

data class Baby(
    val id:String = "",
    val gender: Gender= Gender.GIRL,
    val name: String = "",
    val dateOfBirth: Long=0L,
    val isPremature: Boolean = false,
    val primaryCareGiverIds: List<String> = listOf(),
    val careGiverIds: List<String> = listOf(),
    @Exclude val primaryCareGivers: List<CareGiver> = emptyList(),
    @Exclude val careGivers: List<CareGiver> = emptyList()

) {
    constructor(): this("")
}
