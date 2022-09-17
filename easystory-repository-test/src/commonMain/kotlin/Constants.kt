package ru.otus.otuskotlin.easystory.repository.test

import ru.otus.otuskotlin.easystory.common.models.ESError

const val stubLock = "0000-1111-2222-3333"
const val stubNewLock = "0000-1111-2222-3334"
const val searchString = "2"
val notFoundError: ESError = ESError(field = "id", message = "Not found")