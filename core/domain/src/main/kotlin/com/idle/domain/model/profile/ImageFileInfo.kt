package com.idle.domain.model.profile

data class ImageFileInfo(
    val imageUrl: String,
    val imageFileExtension: MIMEType,
)

enum class MIMEType {
    JPG, PNG, JPEG, GIF, SVG, WEBP, UNKNOWN
}