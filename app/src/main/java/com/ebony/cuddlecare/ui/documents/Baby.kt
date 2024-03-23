package com.ebony.cuddlecare.ui.documents

enum class Gender{
    BOY,
    GIRL
}

//data class Profile(
//    val uuid: String = "",
//    val email: String = "",
//    val firstname: String = "",
//    val lastname:String = "",
//    val primaryCareGiverTo:List<String> = listOf(),
//    val careGiverTo: List<String> = listOf(),
//    val activeBabyId:String? = null
//
//) {
//    constructor() : this("","","","",listOf(), listOf())
//    fun hasAtLeastABabyInCare() = primaryCareGiverTo.isNotEmpty() || careGiverTo.isNotEmpty()
//}
data class Baby(
    val id:String = "",
    val gender: Gender= Gender.GIRL,
    val name: String = "",
    val dateOfBirth: Long=0L,
    val isPremature: Boolean = false,
    val primaryCareGivers: List<String> = listOf(),
    val careGivers: List<String> = listOf()
) {
    constructor(): this("")
}
