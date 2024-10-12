package com.idle.designsystem.compose.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Red = Color(0xffFD4F43)
val Orange050 = Color(0xffFFF7F0)
val Orange100 = Color(0xffFFE4CC)
val Orange200 = Color(0xffFFCA99)
val Orange300 = Color(0xffFFAF66)
val Orange400 = Color(0xffFF9533)
val Orange500 = Color(0xffF97800)
val Orange600 = Color(0xffCC6200)
val Orange700 = Color(0xff994900)

val White000 = Color(0xffffffff)
val Gray050 = Color(0xffF1F2F4)
val Gray100 = Color(0xffD5D7DB)
val Gray200 = Color(0xff9EA3AB)
val Gray300 = Color(0xff7E848F)
val Gray400 = Color(0xff5E6573)
val Gray500 = Color(0xff4B515C)
val Gray600 = Color(0xff383D45)
val Gray700 = Color(0xff26282E)
val Gray800 = Color(0xff131417)
val Black = Color(0xff090A0C)

@Immutable
data class CareColors(
    val red: Color = Red,
    val orange050: Color = Orange050,
    val orange100: Color = Orange100,
    val orange200: Color = Orange200,
    val orange300: Color = Orange300,
    val orange400: Color = Orange400,
    val orange500: Color = Orange500,
    val orange600: Color = Orange600,
    val orange700: Color = Orange700,
    val white000: Color = White000,
    val gray050: Color = Gray050,
    val gray100: Color = Gray100,
    val gray200: Color = Gray200,
    val gray300: Color = Gray300,
    val gray400: Color = Gray400,
    val gray500: Color = Gray500,
    val gray600: Color = Gray600,
    val gray700: Color = Gray700,
    val gray800: Color = Gray800,
    val black: Color = Black
)