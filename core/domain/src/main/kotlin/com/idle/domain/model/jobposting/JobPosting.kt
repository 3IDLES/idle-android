package com.idle.domain.model.jobposting

abstract class JobPosting(
    open val id: String,
    open val distance: Int,
    open val jobPostingType: JobPostingType,
    open val isFavorite: Boolean,
) {
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
