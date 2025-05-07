package no.uio.ifi.in2000.vaeraktiv.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,                   // Bakgrunn for velkomstskjerm
    onBackground = OnBackgroundDark,               // Tekst på bakgrunn
    primary = MonochromeDark,
    onPrimary = MonochromeLight,
    primaryContainer = MonochromeDark,// Rød farge inspirert av pannebånd
    secondary = OnSecondaryBackgroundDark,
    onSecondary = OnSecondaryBackground,      // Farge rundt bokser
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,                  // Bakgrunn for velkomstskjerm
    onBackground = OnBackground,                   // Tekst på bakgrunn
    primary = MonochromeDark,
    onPrimary = MonochromeLight,
    primaryContainer = MonochromeLight,// Rød farge inspirert av pannebånd
    secondary = OnSecondaryBackground,
    onSecondary = OnSecondaryBackground,          // Farge rundt bokser
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