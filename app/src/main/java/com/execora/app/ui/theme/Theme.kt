package com.execora.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class ExecoraExtraColors(
    val terminalBackground: Color = Color.Unspecified,
    val keyword: Color = Color.Unspecified,
    val string: Color = Color.Unspecified,
    val comment: Color = Color.Unspecified,
    val function: Color = Color.Unspecified
)

val LocalExecoraExtraColors = staticCompositionLocalOf { ExecoraExtraColors() }

val DarkExtraColors = ExecoraExtraColors(
    terminalBackground = DarkTerminal,
    keyword = DarkKeyword,
    string = DarkString,
    comment = DarkComment,
    function = DarkFunction
)

val LightExtraColors = ExecoraExtraColors(
    terminalBackground = LightTerminal,
    keyword = LightKeyword,
    string = LightString,
    comment = LightComment,
    function = LightFunction
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface
)

@Composable
fun ExecoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for specific Execora branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extraColors = if (darkTheme) DarkExtraColors else LightExtraColors

    CompositionLocalProvider(LocalExecoraExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object ExecoraTheme {
    val extraColors: ExecoraExtraColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExecoraExtraColors.current
}
