package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class UserId(private val esUserId: String) {
    fun asString() = esUserId

    companion object {
        val NONE = UserId("")
    }
}
