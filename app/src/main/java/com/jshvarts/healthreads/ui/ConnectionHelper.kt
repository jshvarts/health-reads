package com.jshvarts.healthreads.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI

class ConnectionHelper(private val context: Context) {
  fun isConnected(): Boolean {
    val connectivityManager = context
      .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false

    val capabilities =
      connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
      capabilities.hasTransport(TRANSPORT_WIFI) ||
        capabilities.hasTransport(TRANSPORT_CELLULAR) ||
        capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
      else -> false
    }
  }
}