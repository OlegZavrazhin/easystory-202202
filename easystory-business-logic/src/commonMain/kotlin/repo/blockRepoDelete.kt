package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.repository.DBBlockIdRequest
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoDelete(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        val request = DBBlockIdRequest(blockRepoPrepare)
        val result = blockRepo.deleteBlock(request)
        if (!result.isSuccess) {
            state = CORState.FAILING
            errors.addAll(result.errors)
        }
        blockRepoDone = blockRepoRead
    }
}