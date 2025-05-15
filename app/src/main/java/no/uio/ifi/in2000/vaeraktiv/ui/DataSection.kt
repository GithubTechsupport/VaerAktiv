package no.uio.ifi.in2000.vaeraktiv.ui

import androidx.compose.runtime.Composable

/**
 * Displays error, loading or data content based on current state.
 *
 * @param data the nullable data to display
 * @param error optional error message
 * @param loading flag for loading state
 * @param errorMessagePrefix prefix text for error display
 * @param loadingContent composable to show while loading
 * @param content composable to show when data is available
 */
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
