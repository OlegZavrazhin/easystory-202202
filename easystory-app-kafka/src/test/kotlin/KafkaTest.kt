package ru.otus.otuskotlin.easystory.kafka

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.ProducerRecord
import org.rnorth.ducttape.unreliables.Unreliables
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import ru.otus.otuskotlin.easystory.api.v1.apiRequestSerialize
import ru.otus.otuskotlin.easystory.api.v1.apiResponseDeserialize
import ru.otus.otuskotlin.easystory.api.v1.models.*
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KafkaTest {
    val log = KotlinLogging.logger {}

    companion object {
        // check the name
        private val IMAGE = DockerImageName.parse("confluentinc/cp-kafka:latest")
    }

    private val kafka by lazy { KafkaContainer(IMAGE).apply { start() } }
    private val config by lazy { KafkaConfig(hosts = listOf(kafka.bootstrapServers)) }
    private val producer by lazy { config.createProducer() }
    private val consumer by lazy { config.createConsumer() }
    private val topicIn by lazy { config.topicIn }
    private val topicOut by lazy { config.topicOut }
    private val processor by lazy { KafkaProcessor(config = config) }
    private val controller by lazy { KafkaController(setOf(processor)) }

    @BeforeTest
    fun start() {
        controller.start()
    }

    @AfterTest
    fun stop() {
        controller.stop()
    }

    @Test
    fun `kafka create order test`() {
        val request = BlockCreateRequest(
            requestId = "create-request",
            requestType = "create",
            debug = BlockDebug(
                mode = BlockRequestDebugMode.STUB,
                stub = BlockRequestDebugStubs.SUCCESS
            ),
            block = BlockToAddOrUpdate(
                title = "Story of mammals",
                author = "James Gunn",
                content = "<h1>Story of mammals</h1><p>Once upon the time...</p>"
            ),

            )

        val record = ProducerRecord(
            topicIn,
            UUID.randomUUID().toString(),
            apiRequestSerialize(request)
        )

        producer.send(record).get()

        consumer.subscribe(listOf(topicOut))

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS) {
            val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofMillis(100))

            if (records.isEmpty) {
                return@retryUntilTrue false
            }

            val message = records.first()

            log.info { "message.value(): ${message.value()}" }

            val response = apiResponseDeserialize<BlockCreateResponse>(message.value())
            log.info { "response: $response" }

            assertEquals("create-request", response.requestId)
            assertEquals("created block stub", response.block?.id)

            return@retryUntilTrue true
        }

        consumer.unsubscribe()
    }

}