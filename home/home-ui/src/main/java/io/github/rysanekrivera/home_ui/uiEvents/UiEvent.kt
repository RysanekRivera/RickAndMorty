package io.github.rysanekrivera.home_ui.uiEvents

sealed class UiEvent {
    data object Idle: UiEvent()
    data class ShowSnackBar(val message: String): UiEvent()
    data class NavigateToCharacter(val characterId: Int): UiEvent()
    data class ShowAlertDialog(
        val message: String,
        val positiveText: String? = null,
        val negativeText: String? = null,
        val neutralText: String? = null,
        val positiveAction: (() -> Unit)? = null,
        val negativeAction: (() -> Unit)? = null,
        val neutralAction: (() -> Unit)? = null
    ): UiEvent()
}