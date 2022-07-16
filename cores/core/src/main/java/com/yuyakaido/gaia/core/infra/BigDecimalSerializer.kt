package com.yuyakaido.gaia.core.infra

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

@Serializer(forClass = BigDecimal::class)
object BigDecimalSerializer: KSerializer<BigDecimal> {
    override fun serialize(encoder: Encoder, value: BigDecimal) {
        val jsonEncoder = encoder as? JsonEncoder
        jsonEncoder?.encodeJsonElement(JsonPrimitive(value))
    }
    override fun deserialize(decoder: Decoder): BigDecimal {
        val jsonDecoder = decoder as? JsonDecoder
        return BigDecimal(jsonDecoder?.decodeJsonElement()?.jsonPrimitive?.content)
    }
}