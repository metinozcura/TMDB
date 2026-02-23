package com.metinozcura.tmdb.common.util

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Returns true if [throwable] (or its cause chain) indicates no network connectivity,
 * e.g. no internet, DNS failure, or connection refused/timeout.
 */
fun isNoNetwork(throwable: Throwable?): Boolean {
    if (throwable == null) return false
    var t: Throwable? = throwable
    while (t != null) {
        when (t) {
            is UnknownHostException,
            is ConnectException,
            is SocketTimeoutException -> return true
            else -> t = t.cause
        }
    }
    return false
}
