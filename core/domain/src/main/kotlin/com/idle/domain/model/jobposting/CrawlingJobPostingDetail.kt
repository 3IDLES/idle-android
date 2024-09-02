package com.idle.domain.model.jobposting

import java.time.LocalDate
import java.time.ZoneId

data class CrawlingJobPostingDetail(
    override val id: String,
    override val distance: Int,
    override val applyDeadline: LocalDate,
    override val jobPostingType: JobPostingType,
    override val isFavorite: Boolean,
    val title: String,
    val content: String,
    val clientAddress: String,
    val longitude: String,
    val latitude: String,
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
) : JobPosting(id, distance, applyDeadline, jobPostingType, isFavorite)