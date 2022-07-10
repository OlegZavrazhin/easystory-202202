package ru.otus.otuskotlin.easystory.business.logic

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.completeValidation() = worker {
    this.title = "Validation has completed"
    on { state == CORState.RUNNING }
    handle {
        blockValidated = blockCopyForValidation.deepCopy()
    }
}

fun ICorChainDsl<EasyStoryContext>.noSuchStub() = worker {
    this.title = "No such stub"
    on { state == CORState.RUNNING }
    handle {
        state = CORState.FAILING

        errors.add(
            ESError(
                code = "stub-validation",
                group = "validation",
                field = "stub",
                message = "no such stub case ${stubCase.name}"
            )
        )
    }
}