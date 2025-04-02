package com.rysanek.network_utils.coroutines

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.rysanek.network_utils.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

/**
 * Suspends until internet becomes available
 */
private suspend fun waitForInternet(context: Context) = suspendCancellableCoroutine { continuation ->
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {

                // Internet is available, resume the coroutine
                connectivityManager.unregisterNetworkCallback(this)
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }
        }
    }

    // Register for network callbacks
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    // Make sure to unregister callback when coroutine is cancelled
    continuation.invokeOnCancellation {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

/**
 * Extension function for CoroutineScope to launch a coroutine that waits for internet connectivity
 * before executing the provided block, and emits state updates.
 *
 * @param block The suspend function to execute once internet is available
 * @return Flow of NetworkResult states
 */
fun <T> CoroutineScope.launchWithInternetConnectivity(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
) = launch(context) {

    // Emit waiting for network state if we're not connected
    if (!ConnectivityObserver.isConnected.value) {
        ConnectivityObserver.waitForInternet()
    }

    block()

}

/**
 * Extension function for CoroutineScope to launch a coroutine that waits for internet connectivity
 * before executing the provided block, and emits state updates.
 *
 * @param block The suspend function to execute once internet is available
 * @return Flow of NetworkResult states
 */
fun <T> CoroutineScope.launchWithInternetConnectivity(
    networkResultFlow: MutableStateFlow<NetworkResult<T>>,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
) = launch(context) {
    networkResultFlow.emit(NetworkResult.Loading)

    // Emit waiting for network state if we're not connected
    if (!ConnectivityObserver.isConnected.value) {
        networkResultFlow.emit(NetworkResult.WaitingForNetwork)
        ConnectivityObserver.waitForInternet()
    }

    runCatching {
        block()
    }.onSuccess { success ->
        networkResultFlow.emit(NetworkResult.Success(success))
    }.onFailure {
        networkResultFlow.emit(NetworkResult.Error(it))
    }

}

/**
 * Extension function for CoroutineScope to create a deferred that waits for internet connectivity
 * before executing the provided block.
 *
 * @param block The suspend function to execute once internet is available
 * @return A Deferred that will complete with a NetworkResult
 */
fun <T> CoroutineScope.asyncWithInternetConnectivity(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): Deferred<NetworkResult<T>> = async(context) {
    try {
        // Wait for internet before executing
        if (!ConnectivityObserver.isConnected.value) {
            ConnectivityObserver.waitForInternet()
        }

        // Execute the network operation
        val result = block()
        NetworkResult.Success(result)
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }
}

