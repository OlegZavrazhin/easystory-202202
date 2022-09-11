package ru.otus.otuskotlin.easystory.common.repository

import ru.otus.otuskotlin.easystory.common.models.ESBlockFilter

data class DBBlockFilterRequest(
    val filter: ESBlockFilter = ESBlockFilter("")
)