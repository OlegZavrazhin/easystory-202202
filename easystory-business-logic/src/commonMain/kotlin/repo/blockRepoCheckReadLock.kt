package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.helpers.errorConcurrency
import ru.otus.otuskotlin.easystory.common.helpers.fail
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoCheckReadLock(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING && blockValidated.lock != blockRepoRead.lock }
    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been changed: positive lock"))
        blockRepoDone = blockRepoRead
    }
}