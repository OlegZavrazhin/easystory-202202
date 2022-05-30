package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class ESBlockId(private val id: String?) {
    fun asIntOrNull() = id?.toInt()

    companion object {
        val NONE = ESBlockId("")
    }
}