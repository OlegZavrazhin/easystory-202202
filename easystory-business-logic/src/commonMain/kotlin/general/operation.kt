package ru.otus.otuskotlin.easystory.business.logic.general

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESProcess
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.chain

fun ICorChainDsl<EasyStoryContext>.operation(
    title: String,
    process: ESProcess,
    block: ICorChainDsl<EasyStoryContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.process == process && this.state == CORState.RUNNING }
}