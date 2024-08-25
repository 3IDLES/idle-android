package com.idle.network.model.jobposting

import kotlinx.serialization.Serializable

@Serializable
data class ApplyJobPostingRequest(
    val jobPostingId: String,
    val applyMethodType: String,
)