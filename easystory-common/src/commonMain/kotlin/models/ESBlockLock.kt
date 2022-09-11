package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class ESBlockLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ESBlockLock("")
    }
}