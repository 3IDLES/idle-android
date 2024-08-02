package com.idle.network.model.jobposting

import kotlinx.serialization.Serializable

@Serializable
data class JobPostingRequest(
    val weekdays: List<String>,
    val startTime: String,
    val endTime: String,
    val payType: String,
    val payAmount: Int,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val clientName: String,
    val gender: String,
    val birthYear: Int,
    val weight: Int?,
    val careLevel: Int,
    val mentalStatus: String,
    val disease: String?,
    val isMealAssistance: Boolean,
    val isBowelAssistance: Boolean,
    val isWalkingAssistance: Boolean,
    val lifeAssistance: List<String>,
    val speciality: String?,
    val isExperiencePreferred: Boolean,
    val applyMethod: List<String>,
    val applyDeadLineType: String,
    val applyDeadline: String,
)