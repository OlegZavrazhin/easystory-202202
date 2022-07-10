package ru.otus.otuskotlin.easystory.cor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


interface ICorExec<T> {
    suspend fun exec(context: T)
}

interface ICorExecDsl<T> {
    var title: String
    fun build(): ICorExec<T>
}

interface ICorHandlerDsl<T> {
//    check condition
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(Throwable) -> Unit)
}

// for adding worker in chain
interface ICorChainDsl<T> : ICorExecDsl<T>, ICorHandlerDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

// WORKER LOGIC START

// dsl for a worker handle func, here we have funs (functions/methods) and vars from 2 interfaces
interface ICorWorkerDsl<T>: ICorExecDsl<T>, ICorHandlerDsl<T> {
//    any function as an argument
    fun handle(function: suspend T.() -> Unit)
}

interface ICorWorker<T>: ICorExec<T> {
    val title: String
    suspend fun on(context: T): Boolean
    suspend fun handle(context: T)
    suspend fun except(context: T, e: Throwable)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}

class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    override val title: String,
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)

    override suspend fun handle(context: T) {
        execs.forEach { it.exec(context) }
    }

    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)
}

fun <T> ICorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
//    we apply here changes for some context which has been passed
    add(CorWorkerDsl<T>().apply(function))
}

// if we have to pass new constructor
//fun <T> ICorChainDsl<T>.worker(
//    title: String,
//    function: suspend T.() -> Unit
//) {
//    add(
//        CorWorkerDsl<T>().apply {
//            this.title = title
//            this.handle(function)
//        }
//    )
//}

class CorWorkerDsl<T>(
    override var title: String = "",
    var blockOn: suspend T.() -> Boolean = { true },
    var blockExcept: suspend T.(Throwable) -> Unit = {},
    var blockHandle: suspend T.() -> Unit = {}
) : ICorWorkerDsl<T> {

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        blockOn = blockOn,
        blockExcept = blockExcept,
        blockHandle = blockHandle
    )

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(Throwable) -> Unit) {
        blockExcept = function
    }

//    coroutine extension for passed context
    override fun handle(function: suspend T.() -> Unit) {
//        here we assign processed context, it's updated
        blockHandle = function
    }
}

// CorWorker is needed to return after build in CorWorkerDsl with type passed
class CorWorker<T>(
    override val title: String,
    val blockOn: suspend T.() -> Boolean = { true },
    val blockExcept: suspend T.(Throwable) -> Unit = {},
    val blockHandle: suspend T.() -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)

//    some context that was passed from root chain we apply to out worker
    override suspend fun handle(context: T) = blockHandle(context)

    override suspend fun except(context: T, e: Throwable) = blockExcept(context, e)
}

// WORKER LOGIC END

// CHAIN LOGIC START
fun <T> ICorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(
        CorChainDsl<T>().apply(function)
    )
}

class CorChainDsl<T>(
    override var title: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: suspend T.() -> Boolean = { true },
    private var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e },
) : ICorChainDsl<T> {
    override fun build(): ICorExec<T> = CorChain<T>(
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

// CHAIN LOGIC END

// PARALLEL LOGIC START
fun <T> CorChainDsl<T>.parallel(function: CorParallelDsl<T>.() -> Unit) {
    add(
        CorParallelDsl<T>().apply(function)
    )
}

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

fun <T> rootChain(function: CorChainDsl<T>.() -> Unit) = CorChainDsl<T>().apply(function)