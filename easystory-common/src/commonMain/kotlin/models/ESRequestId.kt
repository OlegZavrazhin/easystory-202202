package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class ESRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ESRequestId("")
    }
}