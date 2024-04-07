package com.ebony.cuddlecare.ui.documents

data class DiaperRecord(
    val id: String,
    val diaperType: DiaperType,
    val soilState: List<DiaperSoilType>,
    val timestamp: Long,
    val notes: String,
    val attachmentURL: String,
    val babyId: String
) {
    constructor() : this("",DiaperType.NONE, emptyList(), 0, "", "", "")
}

data class DiaperCount(
    val babyId: String,
    val count: Long,
    val lastRefillEpoch: Long
) {
    constructor() : this("", 0L, 0L)
}

enum class DiaperSoilType {
    WET, DRY, DIRTY
}

enum class DiaperType {
    CLOTH, DISPOSABLE, NONE
}