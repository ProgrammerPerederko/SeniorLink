
package com.seniorlink.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SeniorNavy,
    onPrimary = SeniorWhite,

    secondary = SeniorTeal,
    onSecondary = SeniorWhite,

    tertiary = SeniorMint,
    onTertiary = SeniorNavy,

    background = SeniorBackground,
    onBackground = SeniorText,

    surface = SeniorWhite,
    onSurface = SeniorText,

    error = SeniorEmergency,
    onError = SeniorWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = SeniorTeal,
    onPrimary = SeniorNavy,

    secondary = SeniorMint,
    onSecondary = SeniorNavy,

    tertiary = SeniorNavy,
    onTertiary = SeniorWhite,

    background = SeniorNavy,
    onBackground = SeniorWhite,

    surface = Color(0xFF102F43),
    onSurface = SeniorWhite,

    error = SeniorEmergency,
    onError = SeniorWhite
)

@Composable
fun SeniorLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}