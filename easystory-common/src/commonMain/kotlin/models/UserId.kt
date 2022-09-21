package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class UserId(private val recipeUserId: String) {
    fun asString() = recipeUserId

    companion object {
        val NONE = UserId("")
    }
}
