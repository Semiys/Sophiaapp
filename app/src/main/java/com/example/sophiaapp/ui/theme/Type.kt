package com.example.sophiaapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.sophiaapp.R

// Set of Material typography styles to start with
val CustomFont=FontFamily(
    Font(R.font.rosarium)
)
val Typography = Typography(

    headlineLarge=TextStyle(
        fontFamily=CustomFont,
        fontWeight=FontWeight.Bold,
        fontSize=32.sp,
        lineHeight=40.sp,
        letterSpacing=0.sp

    ),
    headlineMedium=TextStyle(
        fontFamily=CustomFont,
        fontWeight=FontWeight.Bold,
        fontSize=28.sp,
        lineHeight=36.sp,
        letterSpacing=0.sp
    ),
    headlineSmall=TextStyle(
        fontFamily=CustomFont,
        fontWeight=FontWeight.Bold,
        fontSize=24.sp,
        lineHeight=32.sp,
        letterSpacing=0.sp
    ),




    bodyLarge = TextStyle(
        fontFamily = CustomFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium=TextStyle(
        fontFamily=CustomFont,
        fontWeight=FontWeight.Normal,
        fontSize=14.sp,
        lineHeight=20.sp,
        letterSpacing=0.25.sp

    ),
    bodySmall=TextStyle(
        fontFamily=CustomFont,
        fontWeight=FontWeight.Normal,
        fontSize=12.sp,
        lineHeight=16.sp,
        letterSpacing=0.4.sp

    ),

    titleLarge = TextStyle(
        fontFamily = CustomFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    titleMedium=TextStyle(
      fontFamily=CustomFont,
        fontWeight = FontWeight.Medium,
        fontSize=16.sp,
        lineHeight=24.sp,
        letterSpacing=0.15.sp
    ),


    labelSmall = TextStyle(
        fontFamily = CustomFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)