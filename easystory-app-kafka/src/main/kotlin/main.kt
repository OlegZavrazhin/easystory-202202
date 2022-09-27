package ru.otus.otuskotlin.easystory.kafka

import ru.otus.otuskotlin.easystory.common.models.ESSettings
import ru.otus.otuskotlin.easystory.services.BlockService
import ru.otus.otuskotlin.easystory.repository.inmemory.BlockRepoInMemory
import ru.otus.otuskotlin.easystory.repository.postgresql.RepoBlockSQL

fun main(settings: ESSettings? = null) {
    val config = KafkaConfig()

    val corSettings by lazy {
        settings ?: ESSettings(
            repoTest = BlockRepoInMemory(),
            repoProd = RepoBlockSQL(url = "jdbc:postgresql://localhost:6432/esdb")
        )
    }

    val service = BlockService(corSettings)

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