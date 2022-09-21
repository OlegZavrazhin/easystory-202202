package ru.otus.otuskotlin.easystory.business.logic.stubs

import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESStubs
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.stubCreateSuccess(title: String) = worker {
    this.title = "Success block creation stub"
    on { stubCase == ESStubs.SUCCESS && state == CORState.RUNNING }
    handle {
        state = CORState.FINISHING

        blockResponse = Story.getBlock {
            id = ESBlockId("created block stub")
        }

    }
}