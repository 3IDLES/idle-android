package com.idle.domain.model.profile

import java.io.InputStream

data class ImageFileInfo(
    val imageUrl: String,
    val imageFileExtension: MIMEType,
    val imageInputStream: InputStream,
)

enum class MIMEType(val value: String) {
    JPG("image/jpeg"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    SVG("image/svg+xml"),
    WEBP("image/webp"),
    UNKNOWN("unknown");

    companion object {
        fun create(value: String?): MIMEType {
            return MIMEType.entries.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}