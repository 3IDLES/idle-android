package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageUploadUrlResponse(
    val imageId: String = "",
    val imageFileExtension: String = "",
    val uploadUrl: String = "",
)
