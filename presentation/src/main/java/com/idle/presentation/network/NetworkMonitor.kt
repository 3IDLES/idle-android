package com.idle.presentation.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.None)
    val networkState: StateFlow<NetworkState> = _networkState

    private val validTransportTypes = listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR,
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private val networks = mutableSetOf<Network>()

        override fun onAvailable(network: Network) {
            networks += network
            updateNetworkState(networks)
        }

        override fun onLost(network: Network) {
            networks -= network
            updateNetworkState(networks)
        }

        private fun updateNetworkState(networks: Set<Network>) {
            _networkState.value = if (networks.isNotEmpty()) {
                NetworkState.Connected
            } else {
                NetworkState.NotConnected
            }
        }
    }

    private val connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    init {
        connectivityManager?.run {
            initiateNetworkState(this)
            registerNetworkCallback(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initiateNetworkState(manager: ConnectivityManager) {
        _networkState.value = manager.activeNetwork?.let {
            manager.getNetworkCapabilities(it)
        }?.let { networkCapabilities ->
            if (validTransportTypes.any { networkCapabilities.hasTransport(it) }) {
                NetworkState.Connected
            } else {
                NetworkState.NotConnected
            }
        } ?: NetworkState.NotConnected
    }

    @SuppressLint("MissingPermission")
    private fun registerNetworkCallback(manager: ConnectivityManager) {
        NetworkRequest.Builder()
            .apply {
                validTransportTypes.onEach { addTransportType(it) }
            }.let {
                manager.registerNetworkCallback(it.build(), networkCallback)
            }
    }
}

sealed class NetworkState {
    data object None : NetworkState()
    data object Connected : NetworkState()
    data object NotConnected : NetworkState()
}
