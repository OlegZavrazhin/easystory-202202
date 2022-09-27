package ru.otus.otuskotlin.easystory.cor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// PARALLEL LOGIC START
@CORDsl
fun <T> CorChainDsl<T>.parallel(function: CorParallelDsl<T>.() -> Unit) {
    add(
        CorParallelDsl<T>().apply(function)
    )
}

@CORDsl
class CorParallelDsl<T>(
    override var title: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: suspend T.() -> Boolean = { true },
    private var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e },
) : ICorChainDsl<T> {
    override fun build(): ICorExec<T> = CorParallel<T>(
        title = title,
        blockOn = blockOn,
        blockExcept = blockExcept,
        execs = workers.map { it.build() }.toList()
    )

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(Throwable) -> Unit) {
        blockExcept = function
    }

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }
}

class CorParallel<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)

    override suspend fun handle(context: T) = coroutineScope {
        execs.map { launch { it.exec(context) } }.toList().forEach { it.join() }
    }

    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)
}
// PARALLEL LOGIC END