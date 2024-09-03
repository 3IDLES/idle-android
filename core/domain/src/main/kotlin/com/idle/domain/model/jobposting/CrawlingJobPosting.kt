package com.idle.domain.model.jobposting

import java.time.LocalDate

data class CrawlingJobPosting(
    override val id: String,
    override val distance: Int,
    override val jobPostingType: JobPostingType,
    override val applyDeadline: LocalDate,
    override val isFavorite: Boolean,
    val title: String,
    val workingTime: String,
    val workingSchedule: String,
    val payInfo: String,
    val createdAt: LocalDate,
) : JobPosting(id, distance, jobPostingType, applyDeadline, isFavorite)