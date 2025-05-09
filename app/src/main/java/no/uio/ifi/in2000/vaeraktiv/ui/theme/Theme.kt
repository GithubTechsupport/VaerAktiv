package no.uio.ifi.in2000.vaeraktiv.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    primary = MonochromeLight,
    onPrimary = MonochromeDark,
    primaryContainer = MonochromeDark,
    secondary = OnSecondaryBackgroundDark,
    onSecondary = OnSecondaryBackground,
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    onBackground = OnBackground,
    primary = MonochromeDark,
    onPrimary = MonochromeLight,
    primaryContainer = MonochromeLight,
    secondary = OnSecondaryBackground,
    onSecondary = OnSecondaryBackgroundDark,
)


@Composable
fun VaerAktivTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}