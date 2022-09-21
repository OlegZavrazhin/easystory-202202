package ru.otus.otuskotlin.easystory.business.logic.repo

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESBlockFilter
import ru.otus.otuskotlin.easystory.common.repository.DBBlockFilterRequest
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.repoSearch(title: String) = worker {
    this.title = title
    on { state == CORState.RUNNING }
    handle {
        val request = DBBlockFilterRequest(
            filter = ESBlockFilter(searchString = blockFilter.searchString)
        )
        val result = blockRepo.searchBlock(request)
        val resultBlocks = result.result
        if (result.isSuccess && resultBlocks != null) {
            blocksRepoDone = resultBlocks.toMutableList()
        } else {
            state = CORState.FAILING
            errors.addAll(result.errors)
        }
    }
}