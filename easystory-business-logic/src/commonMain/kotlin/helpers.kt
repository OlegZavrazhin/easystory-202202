package ru.otus.otuskotlin.easystory.business.logic

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.buildErrorValidation
import ru.otus.otuskotlin.easystory.common.models.toFailWithError
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.copyToValidationField() = worker {
    this.title = "Deep copy to blockCopyForValidation"
    handle {
        blockCopyForValidation = blockRequest.deepCopy()
    }
}

fun ICorChainDsl<EasyStoryContext>.checkEmptyId() = worker {
    title = "Check id is not empty"
    on { blockCopyForValidation.id.asString().isEmpty() }
    handle {
        toFailWithError(
            buildErrorValidation(
                field = "id",
                violationCode = "empty",
                description = "id should be filled"
            )
        )
    }
}

fun ICorChainDsl<EasyStoryContext>.checkEmptyTitle() = worker {
    title = "Check title is not empty"
    on { blockCopyForValidation.title.isEmpty() }
    handle {
        toFailWithError(buildErrorValidation(
            field = "title",
            violationCode = "empty",
            description = "title should be filled"
        ))
    }
}

fun ICorChainDsl<EasyStoryContext>.checkEmptyContent() = worker {
    title = "Check content is not empty"
    on { blockCopyForValidation.content.isEmpty() }
    handle {
        toFailWithError(buildErrorValidation(
            field = "content",
            violationCode = "empty",
            description = "content should be filled"
        ))
    }
}