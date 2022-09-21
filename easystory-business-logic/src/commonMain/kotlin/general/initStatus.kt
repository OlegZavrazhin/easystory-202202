package ru.otus.otuskotlin.easystory.business.logic.general

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == CORState.NONE }
    handle { state = CORState.RUNNING }
}