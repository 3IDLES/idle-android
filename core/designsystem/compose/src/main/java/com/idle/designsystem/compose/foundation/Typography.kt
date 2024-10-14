package com.idle.designsystem.compose.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.idle.designresource.R

val PretendardBold = FontFamily(
    Font(
        resId = R.font.pretendard_bold,
        weight = FontWeight.Bold,
    ),
)

val PretendardSemiBold = FontFamily(
    Font(
        resId = R.font.pretendard_semi_bold,
        weight = FontWeight.SemiBold,
    ),
)

val PretendardMedium = FontFamily(
    Font(
        resId = R.font.pretendard_medium,
        weight = FontWeight.Medium,
    ),
)

@Immutable
data class CareTypography(
    val heading1: TextStyle = TextStyle(
        fontFamily = PretendardBold,
        fontSize = 26.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.2).sp,
    ),
    val heading2: TextStyle = TextStyle(
        fontFamily = PretendardBold,
        fontSize = 22.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
    ),
    val heading3: TextStyle = TextStyle(
        fontFamily = PretendardBold,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
    ),
    val heading4: TextStyle = TextStyle(
        fontFamily = PretendardBold,
        fontSize = 18.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp,
    ),
    val subtitle1: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 22.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
    ),
    val subtitle2: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
    ),
    val subtitle3: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 18.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp,
    ),
    val subtitle4: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.2).sp,
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp,
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.2).sp,
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
    ),
    val caption1: TextStyle = TextStyle(
        fontFamily = PretendardMedium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
    ),
    val caption2: TextStyle = TextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 12.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
    )
)