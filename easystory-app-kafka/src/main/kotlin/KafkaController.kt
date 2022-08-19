package ru.otus.otuskotlin.easystory.kafka

import kotlinx.coroutines.*
import java.util.concurrent.Executors

class KafkaController(private val processors: Set<KafkaProcessor>) {
    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName("kafka-controller")
    )

    fun start() = scope.launch {
        processors.forEach { processor ->
            launch(
                Executors.newSingleThreadExecutor()
                    .asCoroutineDispatcher() + CoroutineName("kafka-process-${processor.config.groupId}")
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