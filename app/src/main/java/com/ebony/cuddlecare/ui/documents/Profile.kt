data class Profile(
    val uuid: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname:String = "",
    val primaryCareGiverTo:List<String> = listOf(),
    val careGiverTo: List<String> = listOf(),
    val activeBabyId:String? = null

) {
    constructor() : this("","","","",listOf(), listOf())
    fun hasAtLeastABabyInCare() = primaryCareGiverTo.isNotEmpty() || careGiverTo.isNotEmpty()
}