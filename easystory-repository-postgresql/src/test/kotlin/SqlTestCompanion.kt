package ru.otus.otuskotlin.easystory.repository.postgresql

import java.time.Duration

import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import java.util.UUID

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "es"
    private const val PASS = "es-pass"
    private const val SCHEMA = "es"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(initObjects: Collection<ESBlock> = emptyList(), newLock: ESBlockLock = ESBlockLock(UUID.randomUUID().toString())): RepoBlockSQL {
        return RepoBlockSQL(url, USER, PASS, SCHEMA, initObjects, newLock)
    }
}