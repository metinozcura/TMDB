package com.metinozcura.tmdb.common.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkUtilsTest {

    @Test
    fun isNoNetwork_null_returnsFalse() {
        assertFalse(isNoNetwork(null))
    }

    @Test
    fun isNoNetwork_unknownHostException_returnsTrue() {
        assertTrue(isNoNetwork(UnknownHostException()))
    }

    @Test
    fun isNoNetwork_connectException_returnsTrue() {
        assertTrue(isNoNetwork(ConnectException()))
    }

    @Test
    fun isNoNetwork_socketTimeoutException_returnsTrue() {
        assertTrue(isNoNetwork(SocketTimeoutException()))
    }

    @Test
    fun isNoNetwork_genericException_returnsFalse() {
        assertFalse(isNoNetwork(IllegalArgumentException()))
        assertFalse(isNoNetwork(RuntimeException()))
        assertFalse(isNoNetwork(NullPointerException()))
    }

    @Test
    fun isNoNetwork_exceptionWithNoNetworkCause_returnsTrue() {
        val cause = UnknownHostException("Unable to resolve host")
        val wrapper = RuntimeException("Request failed", cause)
        assertTrue(isNoNetwork(wrapper))
    }

    @Test
    fun isNoNetwork_exceptionWithConnectExceptionCause_returnsTrue() {
        val cause = ConnectException("Connection refused")
        val wrapper = RuntimeException(cause)
        assertTrue(isNoNetwork(wrapper))
    }

    @Test
    fun isNoNetwork_exceptionWithSocketTimeoutCause_returnsTrue() {
        val cause = SocketTimeoutException("Read timed out")
        val wrapper = Exception("Network error", cause)
        assertTrue(isNoNetwork(wrapper))
    }

    @Test
    fun isNoNetwork_deepCauseChainWithNoNetwork_returnsTrue() {
        val rootCause = UnknownHostException()
        val level2 = RuntimeException(rootCause)
        val level1 = IllegalStateException(level2)
        assertTrue(isNoNetwork(level1))
    }

    @Test
    fun isNoNetwork_deepCauseChainWithoutNoNetwork_returnsFalse() {
        val rootCause = IllegalArgumentException("Bad argument")
        val level2 = RuntimeException(rootCause)
        val level1 = IllegalStateException(level2)
        assertFalse(isNoNetwork(level1))
    }
}
