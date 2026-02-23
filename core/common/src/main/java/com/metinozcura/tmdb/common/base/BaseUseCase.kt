package com.metinozcura.tmdb.common.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<in P, out R>(private val coroutineContext: CoroutineContext = Dispatchers.IO) {
    operator fun invoke(params: P): Flow<R> {
        return execute(params).flowOn(coroutineContext)
    }

    protected abstract fun execute(params: P): Flow<R>
}
