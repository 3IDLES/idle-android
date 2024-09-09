package com.idle.domain.model.profile

data class CenterRegistrationStatus(
    val id: String,
    val managerName: String,
    val phoneNumber: String,
    val centerManagerAccountStatus: CenterManagerAccountStatus,
)

enum class CenterManagerAccountStatus {
    NEW, PENDING, APPROVED, UNKNOWN;

    companion object {
        fun create(value: String): CenterManagerAccountStatus {
            return CenterManagerAccountStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}