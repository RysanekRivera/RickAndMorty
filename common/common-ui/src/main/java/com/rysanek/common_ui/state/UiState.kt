package com.rysanek.common_ui.state

data class UiState<T>(
    val isLoading: Boolean = true,
    val isWaitingForNetwork: Boolean = false,
    val successData: T? = null,
    val error: Throwable? = null,
){

    fun markLoading() = copy(isLoading = true)

    fun markWaitingForNetwork() = copy(isWaitingForNetwork = true)
}

