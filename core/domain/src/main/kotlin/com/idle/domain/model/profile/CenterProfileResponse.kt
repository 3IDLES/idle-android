package com.idle.domain.model.profile

data class CenterProfile(
    val centerName: String = "",
    val officeNumber: String = "",
    val lotNumberAddress: String = "",
    val detailedAddress: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val introduce: String = "",
    val profileImageUrl: String = "",
)