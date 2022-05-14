package ru.otus.otuskotlin.easystory.common.models

data class ESError (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
)