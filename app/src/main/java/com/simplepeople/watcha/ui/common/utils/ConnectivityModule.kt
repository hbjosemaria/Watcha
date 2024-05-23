package com.simplepeople.watcha.ui.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule  {

    @Provides
    @Singleton
    fun provideConnectivityState(@ApplicationContext context: Context) : ConnectivityState =
        ConnectivityState(context)
}

class ConnectivityState @Inject constructor (
    private val context : Context
) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //Exposed flow to check network connection status changes
    private val _connectivityStateFlow = MutableStateFlow(false)
    val connectivityStateFlow = _connectivityStateFlow.asStateFlow()

    init {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _connectivityStateFlow.value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _connectivityStateFlow.value = false
            }

            override fun onUnavailable() {
                super.onUnavailable()
                _connectivityStateFlow.value = false
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        //Registration of Network callback to receive the events with overrides
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)
    }

}