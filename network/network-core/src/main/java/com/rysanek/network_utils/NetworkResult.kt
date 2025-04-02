package com.rysanek.network_utils

sealed class NetworkResult<out T> {
    data object Idle : NetworkResult<Nothing>()
    data object WaitingForNetwork : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
}