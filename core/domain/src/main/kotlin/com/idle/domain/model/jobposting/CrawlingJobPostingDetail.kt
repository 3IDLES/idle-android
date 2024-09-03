package com.idle.domain.model.jobposting

import java.time.LocalDate

data class CrawlingJobPostingDetail(
    override val id: String,
    override val distance: Int,
    override val jobPostingType: JobPostingType,
    override val applyDeadline: LocalDate,
    override val isFavorite: Boolean,
    val longitude: String,
    val latitude: String,
    val title: String,
    val content: String,
    val clientAddress: String,
    val createdAt: LocalDate,
    val payInfo: String,
    val workingTime: String,
    val workingSchedule: String,
    val recruitmentProcess: String,
    val applyMethod: String,
    val requireDocumentation: String,
    val centerName: String,
    val centerAddress: String,
    val jobPostingUrl: String,
) : JobPosting(id, distance, jobPostingType, applyDeadline, isFavorite)
