package com.idle.domain.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val seoulZone = ZoneId.of("Asia/Seoul")

fun LocalDateTime.formatRelativeDateTime(): String {
    val nowInSeoul = LocalDateTime.now(seoulZone)

    return when {
        this.isAfter(nowInSeoul) -> "미래"

        ChronoUnit.DAYS.between(this.toLocalDate(), nowInSeoul.toLocalDate()) < 1 -> {
            // 하루 이내일 경우: "오후 XX시 XX분"
            val formatter = DateTimeFormatter.ofPattern("a hh:mm").withZone(seoulZone)

            this.format(formatter)
                .replace("AM", "오전")
                .replace("PM", "오후")
        }

        else -> {
            // 하루 이상일 경우: "X월 X일"
            val formatter = DateTimeFormatter.ofPattern("M월 d일").withZone(seoulZone)

            this.format(formatter)
        }
    }
}

fun LocalDateTime.formatRelativeTimeDescription(): String {
    val currentTime = LocalDateTime.now(seoulZone)

    if (this.isAfter(currentTime)) {
        return "미래"
    }

    val minutesDifference = ChronoUnit.MINUTES.between(this, currentTime)
    val hoursDifference = ChronoUnit.HOURS.between(this, currentTime)
    val daysDifference = ChronoUnit.DAYS.between(this, currentTime)

    return when {
        minutesDifference <= 1 -> "방금 전"
        minutesDifference < 60 -> "${minutesDifference}분 전"
        hoursDifference < 24 -> "${hoursDifference}시간 전"
        daysDifference < 7 -> "${daysDifference}일 전"
        else -> "${daysDifference / 7}주 전"
    }
}

fun LocalDateTime.formatTimeToHourMinute24(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(seoulZone)
    return this.format(formatter)
}