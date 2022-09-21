package ru.otus.otuskotlin.easystory.cor

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

@CORDsl
fun <T> ICorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
//    we apply here changes for some context which has been passed
    add(CorWorkerDsl<T>().apply(function))
}

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