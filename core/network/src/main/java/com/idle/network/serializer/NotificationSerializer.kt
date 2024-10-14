package com.idle.network.serializer

import com.idle.domain.model.notification.Notification
import com.idle.domain.model.notification.NotificationContent
import com.idle.domain.model.notification.NotificationType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class NotificationSerializer @Inject constructor() : KSerializer<Notification> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Notification")

    override fun deserialize(decoder: Decoder): Notification {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be loaded only by JSON")
        val jsonObject = input.decodeJsonElement().jsonObject

        // 구분자 필드 파싱
        val notificationTypeString = jsonObject["notificationType"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing 'notificationType' field")
        val notificationType = NotificationType.create(notificationTypeString)

        // 공통 필드 파싱
        val id = jsonObject["id"]?.jsonPrimitive?.content ?: ""
        val isRead = jsonObject["isRead"]?.jsonPrimitive?.boolean ?: false
        val title = jsonObject["title"]?.jsonPrimitive?.content ?: "No title"
        val body = jsonObject["body"]?.jsonPrimitive?.content ?: "No body"
        val imageUrl = jsonObject["imageUrl"]?.jsonPrimitive?.content
        val createdAt = jsonObject["createdAt"]?.jsonPrimitive?.content
            ?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
            ?: LocalDateTime.now()

        val details = jsonObject["notificationDetails"]?.jsonObject

        // 다형성 필드 파싱
        val notificationDetails = details?.let {
            when (notificationType) {
                NotificationType.APPLICANT -> NotificationContent.ApplicantNotification(
                    jobPostingId = details["jobPostingId"]?.jsonPrimitive?.content ?: ""
                )

                NotificationType.UNKNOWN -> NotificationContent.UnKnownNotification
            }
        } ?: NotificationContent.UnKnownNotification

        return Notification(
            notificationType = notificationType,
            id = id,
            isRead = isRead,
            title = title,
            body = body,
            imageUrl = imageUrl,
            createdAt = createdAt,
            notificationDetails = notificationDetails
        )
    }

    override fun serialize(encoder: Encoder, value: Notification) {
        throw UnsupportedOperationException("Serialization is not supported for Notification")
    }
}