package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TufaDarkPrimary,
    onPrimary = TufaDarkBackground,
    primaryContainer = TufaDarkSurface,
    onPrimaryContainer = TufaDarkPrimary,
    secondary = TufaDarkSecondary,
    onSecondary = TufaDarkBackground,
    background = TufaDarkBackground,
    onBackground = TufaDarkPrimary,
    surface = TufaDarkSurface,
    onSurface = TufaDarkPrimary,
    surfaceVariant = TufaDarkSurface,
    onSurfaceVariant = TufaDarkSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = TufaCharcoal,
    onPrimary = TufaWarmBeige,
    primaryContainer = TufaOatmeal,
    onPrimaryContainer = TufaCharcoal,
    secondary = TufaClay,
    onSecondary = TufaWhite,
    background = TufaWarmBeige,
    onBackground = TufaCharcoal,
    surface = TufaWhite,
    onSurface = TufaCharcoal,
    surfaceVariant = TufaOatmeal,
    onSurfaceVariant = TufaClay
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Force premium brand identity colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
