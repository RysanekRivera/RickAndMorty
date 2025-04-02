package com.rysanek.common_ui.uievents

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
}
