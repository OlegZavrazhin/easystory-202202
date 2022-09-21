package ru.otus.otuskotlin.easystory.business.logic.stubs

import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESStubs
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.stubSearchSuccess(title: String) = worker {
    this.title = "Success block stub during search"
    on { stubCase == ESStubs.SUCCESS && state == CORState.RUNNING }

    handle {
        state = CORState.FINISHING

        blocksResponse = Story.getBlocks()
    }

}