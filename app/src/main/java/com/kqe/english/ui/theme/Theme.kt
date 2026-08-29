package com.kqe.english.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KqeColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = White,
    primaryContainer = BrandBlue,
    onPrimaryContainer = White,
    secondary = BrandBlue,
    onSecondary = White,
    background = Ink900,
    onBackground = White,
    surface = Navy800,
    onSurface = White,
    surfaceVariant = Navy700,
    onSurfaceVariant = GrayBlue,
    outline = Divider,
    outlineVariant = Divider,
    error = DangerRed,
    onError = White
)

/**
 * 应用全局深色主题（固定深色，不随系统切换）。
 */
@Composable
fun KqeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KqeColorScheme,
        typography = KqeTypography,
        content = content
    )
}
