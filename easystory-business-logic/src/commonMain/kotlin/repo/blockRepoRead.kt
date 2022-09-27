package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.repository.DBBlockIdRequest
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoRead(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        val request = DBBlockIdRequest(blockValidated)
        val result = blockRepo.readBlock(request)
        val resultBlock = result.result
        if (result.isSuccess && resultBlock != null) {
            blockRepoRead = resultBlock
        } else {
            state = CORState.FAILING
            errors.addAll(result.errors)
        }
    }
}