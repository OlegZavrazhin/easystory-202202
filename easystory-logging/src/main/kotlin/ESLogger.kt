package ru.otus.otuskotlin.easystory.logging

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

suspend fun <T> Logger.wrapWithLogging(
    id: String = "",
    block: suspend () -> T,
): T = try {
    val timeStart = Instant.now()
    info("Entering $id")
    block().also {
        val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
        info("Finishing $id", Pair("metricHandleTime", diffTime))
    }
} catch (e: Throwable) {
    error("Failing $id", e)
    throw e
}

fun esLogger(loggerId: String): ESLogWrapper = ESLogWrapper(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun esLogger(cls: Class<out Any>): ESLogWrapper = ESLogWrapper(
    logger = LoggerFactory.getLogger(cls) as Logger
)

/**
 * Generate internal esLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun esLogger(logger: Logger): ESLogWrapper = ESLogWrapper(
    logger = logger,
    loggerId = logger.name,
)
