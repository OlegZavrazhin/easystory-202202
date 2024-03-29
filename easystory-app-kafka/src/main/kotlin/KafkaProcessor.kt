package ru.otus.otuskotlin.easystory.kafka

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiRequestDeserialize
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiResponseSerialize
import ru.otus.otuskotlin.easystory.api.v1.models.IRequest
import ru.otus.otuskotlin.easystory.api.v1.models.IResponse
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.mappers.jackson.fromTransport
import ru.otus.otuskotlin.easystory.mappers.jackson.toTransportBlock
import ru.otus.otuskotlin.easystory.services.BlockService
import java.time.Duration
import java.util.*
import kotlinx.atomicfu.atomic
import ru.otus.otuskotlin.easystory.common.models.ESSettings

private val log = KotlinLogging.logger {}

class KafkaProcessor(
    val config: KafkaConfig,
    private val service: BlockService = BlockService(ESSettings()),
    private val consumer: Consumer<String, String> = config.createConsumer(),
    private val producer: Producer<String, String> = config.createProducer()
) {
    private val process = atomic(true)
    fun process() = runBlocking {
        try {
            consumer.subscribe(listOf(config.topicIn))

            val context = EasyStoryContext(timeStart = Clock.System.now())

            while (process.value) {

                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }

                if (!records.isEmpty)
                    log.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val recordValue = record.value()
                        log.info { "process ${record.key()} from ${record.topic()}:\n${recordValue}" }

                        val request: IRequest = apiRequestDeserialize(recordValue)
                        context.fromTransport(request)

                        service.exec(context)
                        sendResponse(context)

                    } catch (e: Throwable) {
                        log.error(e) { "error" }
                    }
                }

            }

        } catch (e: WakeupException) {
            // why no handling, but catching
        } catch (e: RuntimeException) {
            withContext(NonCancellable) {
                throw e
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: EasyStoryContext) {
        val response: IResponse = context.toTransportBlock()
        val json = apiResponseSerialize(response)
        val record = ProducerRecord(config.topicOut, UUID.randomUUID().toString(), json)

        log.info { "sending ${record.key()} to ${config.topicOut}:\n$json" }

        runBlocking {
            withContext(Dispatchers.IO ) {
                producer.send(record)
            }
        }
    }

    fun stop() {
        process.value = false
    }
}