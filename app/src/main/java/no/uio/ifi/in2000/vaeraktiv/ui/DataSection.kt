package no.uio.ifi.in2000.vaeraktiv.ui

import androidx.compose.runtime.Composable

// The class is a reusable component that can be used where data is loaded into the UI.
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
