package ru.otus.otuskotlin.easystory.common.repository

import ru.otus.otuskotlin.easystory.common.models.ESError

interface IDBResponse<T> {
    val result: T?
    val isSuccess: Boolean
    val errors: List<ESError>
}