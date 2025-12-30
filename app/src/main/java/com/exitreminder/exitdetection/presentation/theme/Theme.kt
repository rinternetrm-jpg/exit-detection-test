package com.exitreminder.exitdetection.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Brand Colors
val Primary = Color(0xFF4A90D9)
val PrimaryDark = Color(0xFF3A7BC8)
val Secondary = Color(0xFF6B7280)

// Background
val Background = Color(0xFF0F172A)
val Surface = Color(0xFF1E293B)
val SurfaceLight = Color(0xFF334155)

// Text
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// Status Colors
val StatusHome = Color(0xFF22C55E)  // Green
val StatusLeaving = Color(0xFFF97316)  // Orange
val StatusOutside = Color(0xFFEF4444)  // Red
val StatusUncertain = Color(0xFFEAB308)  // Yellow

// Signal Colors
val SignalExcellent = Color(0xFF22C55E)
val SignalGood = Color(0xFF84CC16)
val SignalFair = Color(0xFFEAB308)
val SignalWeak = Color(0xFFF97316)
val SignalBad = Color(0xFFEF4444)

// Factor Colors
val FactorPositive = Color(0xFF22C55E)
val FactorNegative = Color(0xFFEF4444)
val FactorNeutral = Color(0xFF94A3B8)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    error = StatusOutside,
    onError = Color.White
)

@Composable
fun ExitDetectionTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            try {
                (view.context as? Activity)?.let { activity ->
                    activity.window.statusBarColor = Background.toArgb()
                    WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
                }
            } catch (e: Exception) {
                // Ignore - status bar color is just cosmetic
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val Typography = Typography()

// Helper functions
fun getSignalColor(dbm: Int): Color {
    return when {
        dbm >= -50 -> SignalExcellent
        dbm >= -60 -> SignalGood
        dbm >= -70 -> SignalFair
        dbm >= -80 -> SignalWeak
        else -> SignalBad
    }
}

fun getStatusColor(probability: Float): Color {
    return when {
        probability < 0.3f -> StatusHome
        probability < 0.6f -> StatusUncertain
        probability < 0.8f -> StatusLeaving
        else -> StatusOutside
    }
}
