package com.idle.network.model.jobposting

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteJobPostingRequest(
    val jobPostingType: String,
)
