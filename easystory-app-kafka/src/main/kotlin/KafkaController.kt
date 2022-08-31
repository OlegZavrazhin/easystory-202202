package ru.otus.otuskotlin.easystory.kafka

import kotlinx.coroutines.*

class KafkaController(private val processors: Set<KafkaProcessor>) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = CoroutineScope(
        Dispatchers.IO.limitedParallelism(1)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun start() = scope.launch {
        processors.forEach { processor ->
            launch(
                Dispatchers.IO.limitedParallelism(1)
            ) {
                try {
                    processor.process()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun stop() = scope.cancel("kafka has been stopped")
}