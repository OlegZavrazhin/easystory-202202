package ru.otus.otuskotlin.easystory.business.logic.stubs

import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.common.models.ESStubs
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.stubBadId(title: String) = worker {
    this.title = title
    on { stubCase == ESStubs.BAD_ID && state == CORState.RUNNING }
    handle {
        state = CORState.FAILING

        errors.add(
            ESError(
                code = "id-validation",
                group = "validation",
                field = "id",
                message = "id not valid"
            )
        )

        blockResponse = Story.getBlock {
            id = blockRequest.id
        }

    }
}