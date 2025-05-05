package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.runtime.Composable

// Klassen er en gjenbrukbar komponent som kan brukes der man laster data i UI'en.
@Composable
fun <T> DataSection(
    data: T?,
    error: String?,
    loading: Boolean = false,
    errorMessagePrefix: String,
    loadingContent: @Composable () -> Unit = {},
    content: @Composable (T) -> Unit
) {
    when {
        error != null -> ErrorMessage("$errorMessagePrefix: $error")
        loading -> loadingContent()
        data != null -> content(data)
    }
}
