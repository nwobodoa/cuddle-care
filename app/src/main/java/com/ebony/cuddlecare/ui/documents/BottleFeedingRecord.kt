package com.ebony.cuddlecare.ui.documents

data class BottleFeedingRecord(
    val id: String,
    val babyId: String,
    val quantityMl: Long,
    val notes: String,
    val attachmentURL: String,
    val timestamp: Long,
) : SortableActivity {
    constructor() : this("", "", 0, "", "", 0)

    override fun rank(): Long {
        return timestamp
    }
}