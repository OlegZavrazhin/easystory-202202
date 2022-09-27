package ru.otus.otuskotlin.easystory.common.models

data class ESBlockPrincipalMode(
    val id: UserId = UserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
) {
    companion object {
        val NONE = ESBlockPrincipalMode()
        val user = ESBlockPrincipalMode(
            UserId("user"),
            "Jonny",
            "Middle",
            "Smith"
        )
    }
}