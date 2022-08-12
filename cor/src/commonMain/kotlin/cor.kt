package ru.otus.otuskotlin.easystory.cor


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

fun <T> rootChain(function: CorChainDsl<T>.() -> Unit) = CorChainDsl<T>().apply(function)