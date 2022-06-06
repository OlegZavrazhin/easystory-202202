package ru.otus.otuskotlin.easystory.app.v1

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.easystory.app.v1.models.IResponse

// this is singletone
internal object BlockResponseSerializer : JsonContentPolymorphicSerializer<IResponse>(IResponse::class) {
    private const val DISCRIMINATOR = "responseType"
    override fun selectDeserializer(element: JsonElement): KSerializer<out IResponse> {
        return when (val discriminatorValue = element.jsonObject[DISCRIMINATOR]?.jsonPrimitive?.content) {
            "create" -> ResponseSerializers.create
            "read" -> ResponseSerializers.read
            "update" -> ResponseSerializers.update
            "delete" -> ResponseSerializers.delete
            "search" -> ResponseSerializers.search
            else -> throw SerializationException("Unknown discriminator value: '${discriminatorValue}'")
        }
    }
}