package com.ebony.cuddlecare.ui.documents

import java.time.LocalDate
import java.util.Date

enum class Gender{
    BOY,
    GIRL
}
data class Baby(
    val id:String,
    val gender: Gender,
    val name: String,
    val dateOfBirth: Long,
    val isPremature: Boolean,
    val primaryCareGivers: List<String> = listOf(),
    val careGivers: List<String> = listOf()
)
