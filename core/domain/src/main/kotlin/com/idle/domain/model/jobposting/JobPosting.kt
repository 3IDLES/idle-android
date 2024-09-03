package com.idle.domain.model.jobposting

import java.time.LocalDate
import java.time.ZoneId

open class JobPosting(
    open val id: String,
    open val distance: Int,
    open val jobPostingType: JobPostingType,
    open val applyDeadline: LocalDate,
    open val isFavorite: Boolean,
) {
    fun calculateDeadline(): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val nowDate = LocalDate.now(seoulZone)
        return applyDeadline.toEpochDay() - nowDate.toEpochDay()
    }

    fun getDistanceInMinutes(): String = when (distance) {
        in 0..200 -> "5분 이내"
        in 201..400 -> "5~10분"
        in 401..700 -> "10~15분"
        in 701..1000 -> "15~20분"
        in 1001..1250 -> "20~25분"
        in 1251..1500 -> "25~30분"
        in 1501..1750 -> "30~35분"
        in 1751..2000 -> "35~40분"
        else -> "40분 이상"
    }
}

open class JobPostingDetail(
    override val id: String,
    override val distance: Int,
    override val jobPostingType: JobPostingType,
    override val applyDeadline: LocalDate,
    override val isFavorite: Boolean,
    open val latitude: String,
    open val longitude: String,
) : JobPosting(id, distance, jobPostingType, applyDeadline, isFavorite)