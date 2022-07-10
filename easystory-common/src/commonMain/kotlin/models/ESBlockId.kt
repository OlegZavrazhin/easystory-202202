package ru.otus.otuskotlin.easystory.common.models

@JvmInline
value class ESBlockId(private val id: String?) {
    fun asIntOrNull() = id?.toInt()
    fun asStringOrNull() = id?.toString()
    fun asString() = id.toString()

    companion object {
        val NONE = ESBlockId("")
    }
}