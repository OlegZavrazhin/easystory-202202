package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.repository.DBBlockRequest
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoCreate(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        val request = DBBlockRequest(blockRepoPrepare)
        val result = blockRepo.createBlock(request)
        val resultBlock = result.result
        println("repoCreate here resultBlock: ${resultBlock}")
        if (result.isSuccess && resultBlock != null) {
            blockRepoDone = resultBlock
        } else {
            state = CORState.FAILING
            errors.addAll(result.errors)
        }
    }
}