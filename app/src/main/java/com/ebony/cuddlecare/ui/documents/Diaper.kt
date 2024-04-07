package com.ebony.cuddlecare.ui.documents

import com.ebony.cuddlecare.util.epochSecondsToLocalDateTime
import java.time.format.DateTimeFormatter

interface SortableActivity{
    fun rank():Long
}
data class DiaperRecord(
    val id: String,
    val diaperType: DiaperType,
    val soilState: List<DiaperSoilType>,
    val timestamp: Long,
    val notes: String,
    val attachmentURL: String,
    val babyId: String
) : SortableActivity {
    constructor() : this("",DiaperType.NONE, emptyList(), 0, "", "", "")
    fun timestampToString(): String {
       return epochSecondsToLocalDateTime(timestamp)?.format( DateTimeFormatter.ofPattern("h:mm a")) ?: ""
    }

    override fun rank(): Long {
        return  timestamp
    }
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