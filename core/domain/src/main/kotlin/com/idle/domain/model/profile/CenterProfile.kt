package com.idle.domain.model.profile

data class CenterProfile(
    val centerName: String,
    val officeNumber: String,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val detailedAddress: String,
    val longitude: Double,
    val latitude: Double,
    val introduce: String?,
    val profileImageUrl: String?,
)