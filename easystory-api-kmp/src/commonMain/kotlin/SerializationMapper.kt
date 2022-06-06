package ru.otus.otuskotlin.easystory.app.v1

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import ru.otus.otuskotlin.easystory.app.v1.models.*

// here I have questions about how it works
@OptIn(ExperimentalSerializationApi::class)
internal val serializationMapper = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        polymorphicDefaultSerializer(IRequest::class) { instance ->
            @Suppress("UNCHECKED_CAST")
            when (instance) {
                is BlockCreateRequest -> RequestSerializers.create as SerializationStrategy<IRequest>
                is BlockReadRequest   -> RequestSerializers.read as SerializationStrategy<IRequest>
                is BlockUpdateRequest -> RequestSerializers.update as SerializationStrategy<IRequest>
                is BlockDeleteRequest -> RequestSerializers.delete as SerializationStrategy<IRequest>
                is BlockSearchRequest -> RequestSerializers.search as SerializationStrategy<IRequest>
                else -> null
            }
        }
        polymorphicDefault(IRequest::class) {
            BlockRequestSerializer
        }
        polymorphicDefaultSerializer(IResponse::class) { instance ->
            @Suppress("UNCHECKED_CAST")
            when (instance) {
                is BlockCreateResponse -> ResponseSerializers.create as SerializationStrategy<IResponse>
                is BlockReadResponse   -> ResponseSerializers.read as SerializationStrategy<IResponse>
                is BlockUpdateResponse -> ResponseSerializers.update as SerializationStrategy<IResponse>
                is BlockDeleteResponse -> ResponseSerializers.delete as SerializationStrategy<IResponse>
                is BlockSearchResponse -> ResponseSerializers.search as SerializationStrategy<IResponse>
                else -> null
            }
        }
        polymorphicDefault(IResponse::class) {
            BlockResponseSerializer
        }
    }
}