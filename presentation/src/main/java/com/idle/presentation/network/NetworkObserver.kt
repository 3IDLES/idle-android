package com.idle.presentation.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkObserver @Inject constructor(context: Context) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.NONE)
    val networkState = _networkState.asStateFlow()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkState.value = NetworkState.CONNECTED
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _networkState.value = NetworkState.NOT_CONNECTED
        }
    }

    init {
        checkNetworkState()
        subscribeNetworkCallback()
    }

    internal fun checkNetworkState() {
        val caps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (caps == null) {
            _networkState.value = NetworkState.NOT_CONNECTED
            return
        }

        _networkState.value =
            if (caps.hasTransport(TRANSPORT_WIFI) || caps.hasTransport(TRANSPORT_CELLULAR)) {
                NetworkState.CONNECTED
            } else {
                NetworkState.NOT_CONNECTED
            }
    }

    internal fun unsubscribeNetworkCallback() =
        connectivityManager.unregisterNetworkCallback(networkCallback)

    private fun subscribeNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
}

enum class NetworkState {
    NONE, CONNECTED, NOT_CONNECTED
}