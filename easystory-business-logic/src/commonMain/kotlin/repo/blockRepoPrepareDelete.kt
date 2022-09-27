package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        blockRepoPrepare = blockValidated.deepCopy()
    }
}