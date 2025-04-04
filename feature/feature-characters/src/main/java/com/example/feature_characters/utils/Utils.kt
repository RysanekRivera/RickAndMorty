package com.example.feature_characters.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.rysanek.common_exceptions.SuccessNoResponseException
import com.rysanek.common_ui.state.UiState
import com.rysanek.network_utils.NetworkResult
import retrofit2.Response

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun <T> UiState<T>.updateUiStateFromNetworkResult(networkResult: NetworkResult<T>): UiState<T> = when (networkResult) {
    is NetworkResult.Success -> {
        this.copy(
            successData = networkResult.data,
            error = null,
            isLoading = false,
            isWaitingForNetwork = false
        )
    }

    is NetworkResult.Error -> {
        this.copy(
            error = networkResult.exception,
            isLoading = false,
            isWaitingForNetwork = false
        )
    }

    NetworkResult.Loading -> markLoading()

    NetworkResult.WaitingForNetwork -> markWaitingForNetwork()

    NetworkResult.Idle -> this
}

fun <T> UiState<T>.updateUiStateFromNetworkResponse(networkResult: NetworkResult<Response<T>>): UiState<T> = when (networkResult) {
    is NetworkResult.Success -> {
        if (networkResult.data.isSuccessful && networkResult.data.body() != null) {
            copy(
                isLoading = false,
                isWaitingForNetwork = false,
                successData = networkResult.data.body()
            )
        } else {
            copy(
                isLoading = false,
                isWaitingForNetwork = false,
                error = SuccessNoResponseException()
            )
        }
    }

    is NetworkResult.Error -> {
        copy(
            isLoading = false,
            isWaitingForNetwork = false,
            error = networkResult.exception
        )
    }

    is NetworkResult.Loading -> markLoading()

    is NetworkResult.WaitingForNetwork -> markWaitingForNetwork()

    else -> this
}