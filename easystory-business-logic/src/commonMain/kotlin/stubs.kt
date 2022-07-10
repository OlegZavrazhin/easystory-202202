package ru.otus.otuskotlin.easystory.business.logic

import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.common.models.ESStubs
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.stubSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ESStubs.SUCCESS && state == CORState.RUNNING }

    handle {
        state = CORState.FINISHING

        blockResponse = Story.getBlock() {
            id  = blockRequest.id
        }
    }
}

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

fun ICorChainDsl<EasyStoryContext>.stubBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == ESStubs.BAD_TITLE && state == CORState.RUNNING }
    handle {
        state = CORState.FAILING

        errors.add(
            ESError(
                code = "title-validation",
                group = "validation",
                field = "title",
                message = "title not valid"
            )
        )

        blockResponse = Story.getBlock {
            id = ESBlockId("block stub BAD_TITLE")
        }

    }
}