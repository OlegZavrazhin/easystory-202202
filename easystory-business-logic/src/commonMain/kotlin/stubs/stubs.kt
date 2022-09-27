package ru.otus.otuskotlin.easystory.business.logic.stubs

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESWorkMode
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.chain

fun ICorChainDsl<EasyStoryContext>.stubs(
    title: String,
    block: ICorChainDsl<EasyStoryContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.workMode == ESWorkMode.STUB && state == CORState.RUNNING }
}