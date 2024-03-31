package com.ebony.cuddlecare.ui.documents

import com.google.firebase.firestore.Exclude

data class CareGiver(
    val uuid: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname:String = "",
    val primaryCareGiverTo:List<String> = listOf(),
    val careGiverTo: List<String> = listOf(),
    val activeBabyId:String? = null,
    @Exclude val pendingInvites: List<Invite> = emptyList()

) {
    constructor() : this("","","","",listOf(), listOf())
    fun hasAtLeastABabyInCare() = primaryCareGiverTo.isNotEmpty() || careGiverTo.isNotEmpty()
}