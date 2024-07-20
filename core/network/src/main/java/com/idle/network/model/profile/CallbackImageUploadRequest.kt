package com.idle.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class CallbackImageUploadRequest(
    val imageId: String,
    val imageFileExtension: String,
)
