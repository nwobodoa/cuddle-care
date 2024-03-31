package com.ebony.cuddlecare.ui.documents

enum class InviteStatus {
    PENDING, ACCEPTED, REJECTED;
}

data class Invite(
    val id: String,
    val careGiverEmail: String = "",
    val primaryCareGiverId: String = "",
    val status: InviteStatus = InviteStatus.PENDING,
    val babyId: String = "",
    val babyName:String = ""

) {
    constructor() : this("")
}