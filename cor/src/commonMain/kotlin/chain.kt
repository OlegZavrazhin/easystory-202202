package ru.otus.otuskotlin.easystory.cor

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