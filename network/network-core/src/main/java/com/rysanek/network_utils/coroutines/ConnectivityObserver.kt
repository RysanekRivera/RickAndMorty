package com.rysanek.network_utils.coroutines

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

/**
 * Singleton manager for observing internet connectivity changes across the app
 */
object ConnectivityObserver {
    // StateFlow ensures all new subscribers get the most recent connectivity state
    private val _isConnected = MutableStateFlow(false)

    // Public immutable flow that can be collected by any component
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private lateinit var connectivityManager: ConnectivityManager
    private var isInitialized = false

    /**
     * Initialize the observer with the application context and validation options
     * @param application Application context
     */
    fun initialize(application: Application) {
        if (isInitialized) return

        connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Register for network callbacks
        val networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        isInitialized = true
    }

    private fun createNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                if (capabilities != null && hasInternetCapability(capabilities)) {
                    _isConnected.value = true
                }
            }

            override fun onLost(network: Network) {
                // Check if there are other networks still available
                if (!isInternetAvailable()) {
                    _isConnected.value = false
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet = hasInternetCapability(networkCapabilities)
                _isConnected.value = hasInternet
            }
        }
    }

    /**
     * Checks if the network capabilities indicate internet availability
     * @param capabilities The network capabilities to check
     */
    private fun hasInternetCapability(
        capabilities: NetworkCapabilities,
    ): Boolean {
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return hasInternetCapability(capabilities)
    }

    /**
     * Suspends until internet becomes available
     */
    suspend fun waitForInternet() {
        // If already connected, return immediately
        if (isConnected.value) return

        // Otherwise, wait for the connection to become available
        isConnected
            .filter { it } // Only care about true (connected) values
            .first()       // Take the first one and complete
    }
}

/**
 * Extension function to make the Application class initialize the ConnectivityObserver
 */
fun Application.initializeConnectivityObserver() {
    ConnectivityObserver.initialize(this)
}
