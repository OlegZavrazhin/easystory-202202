package ru.otus.otuskotlin.easystory.api.kmp.v1

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.easystory.api.v1.models.IRequest

// this is singletone
internal object BlockRequestSerializer : JsonContentPolymorphicSerializer<IRequest>(IRequest::class) {
    private const val DISCRIMINATOR = "requestType"
    override fun selectDeserializer(element: JsonElement): KSerializer<out IRequest> {
        println("ELEMENT: ${element.jsonObject["block"]?.jsonObject?.get("props")}")
        return when (val discriminatorValue = element.jsonObject[DISCRIMINATOR]?.jsonPrimitive?.content) {
            "create" -> RequestSerializers.create
            "read" -> RequestSerializers.read
            "update" -> RequestSerializers.update
            "delete" -> RequestSerializers.delete
            "search" -> RequestSerializers.search
            else -> throw SerializationException("Unknown discriminator value: '${discriminatorValue}'")
        }
    }
}