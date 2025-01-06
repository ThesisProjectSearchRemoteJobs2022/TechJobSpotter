package com.rogergcc.techjobspotter.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

object InternetCheck {

    suspend fun isNetworkAvailable(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            socket.connect(socketAddress)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}