package com.idle.network.serializer

import com.idle.network.model.chat.ChatMessageResponse
import com.idle.network.model.chat.ChatResponse
import com.idle.network.model.chat.ChatResponse.Companion.MESSAGE_TYPE
import com.idle.network.model.chat.ChatResponse.Companion.READ_TYPE
import com.idle.network.model.chat.ReadMessageResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class ChatResponseSerializer @Inject constructor() :
    JsonContentPolymorphicSerializer<ChatResponse>(ChatResponse::class) {
    override fun selectDeserializer(element: JsonElement) =
        when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            MESSAGE_TYPE -> ChatMessageResponse.serializer()
            READ_TYPE -> ReadMessageResponse.serializer()
            else -> throw SerializationException("알 수 없는 타입: ${element.jsonObject["type"]}")
        }
}
