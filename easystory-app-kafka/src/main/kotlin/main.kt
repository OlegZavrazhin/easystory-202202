package ru.otus.otuskotlin.easystory.kafka

import ru.otus.otuskotlin.easystory.services.BlockService

fun main() {
    val config = KafkaConfig()

    val service = BlockService()

    val processor by lazy {
        KafkaProcessor(
            config = config,
            service = service
        )
    }

    val controller by lazy {
        KafkaController(processors = setOf(processor))
    }

    controller.start()
}