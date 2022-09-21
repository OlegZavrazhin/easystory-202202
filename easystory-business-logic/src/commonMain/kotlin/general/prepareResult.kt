package ru.otus.otuskotlin.easystory.business.logic.general

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESWorkMode
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.prepareResult(title: String) = worker {
    // TODO: maybe some HOC for the title
    this.title = title
    on { workMode != ESWorkMode.STUB }
    handle {
        blockResponse = blockRepoDone
        blocksResponse = blocksRepoDone
        state = when (val st = state) {
            CORState.RUNNING -> CORState.FINISHING
            else -> st
        }
    }
}