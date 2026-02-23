package com.metinozcura.tmdb.common.base

/**
 * Generic call state for UI that loads data. Use instead of separate [isLoading], [error], [isEmpty] flags.
 *
 * Feature modules can use [CallState] with their data type, e.g. [CallState]<List<MovieItemUiData>>.
 */
sealed interface CallState<out T> {

    /** Data is being loaded. */
    data object Loading : CallState<Nothing>

    /** No network connectivity (e.g. no internet). */
    data object NoNetwork : CallState<Nothing>

    /** Load failed with an optional message and optional error code. */
    data class Error(val message: String, val errorCode: String? = null) : CallState<Nothing>

    /** Load succeeded but result is empty (e.g. no results for current filters). */
    data object NoResults : CallState<Nothing>

    /** Load succeeded with [data]. */
    data class Success<T>(val data: T) : CallState<T>
}
