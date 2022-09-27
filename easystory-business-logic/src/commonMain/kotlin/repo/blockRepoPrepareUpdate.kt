package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        blockRepoPrepare = blockRepoRead.deepCopy().apply {
            this.title = blockValidated.title // TODO: why only for title
            content = blockValidated.content
        }
    }
}