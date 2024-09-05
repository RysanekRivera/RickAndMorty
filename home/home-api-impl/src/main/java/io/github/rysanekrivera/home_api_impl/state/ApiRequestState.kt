package io.github.rysanekrivera.home_api_impl.state

import kotlinx.coroutines.flow.MutableStateFlow

sealed class ApiRequestState {

    companion object {
        fun success() : ApiRequestState = Success
        fun successNoContent(): ApiRequestState = SuccessNoContent
        fun error(): ApiRequestState = Error
        fun idle(): ApiRequestState = Idle
    }

    data object Success : ApiRequestState()
    data object Error : ApiRequestState()
    data object SuccessNoContent : ApiRequestState()
    data object Idle : ApiRequestState()
}

inline fun <reified S> MutableStateFlow<S>.updateValue(block: S.() -> S) {
    while (true) {
        val prevValue = value
        val nextValue = block(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}




