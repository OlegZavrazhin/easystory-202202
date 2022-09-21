package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.repository.DBBlockRequest
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        val request = DBBlockRequest(
            blockRepoPrepare.deepCopy().apply {
                this.title = blockValidated.title // TODO: why only for title
                content = blockValidated.content
            }
        )
        val result = blockRepo.updateBlock(request)
        val resultBlock = result.result
        if (result.isSuccess && resultBlock != null) {
            blockRepoDone = resultBlock
        } else {
            state = CORState.FAILING
            errors.addAll(result.errors)
            blockRepoDone
        }
    }
}