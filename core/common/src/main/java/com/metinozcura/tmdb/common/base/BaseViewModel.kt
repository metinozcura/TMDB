package com.metinozcura.tmdb.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Base ViewModel with MVI: intents → reducer → state + effects.
 */
abstract class BaseViewModel<Intent : Reducer.Intent, State : Reducer.State, Effect : Reducer.Effect>(
    initialState: State,
    private val reducer: Reducer<Intent, State, Effect>
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>(
        replay = 0,
        extraBufferCapacity = 32
    )
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    init {
        observeEffects()
    }

    fun sendIntent(event: Intent) {
        val (newState, effect) = reducer.reduce(_state.value, event)
        updateState(newState)
        effect?.let { sendEffect(it) }
    }

    fun observeEffects() {
        effect.onEach { effect ->
            handleEffect(effect)
        }.launchIn(viewModelScope)
    }

    abstract fun handleEffect(effect: Effect)

    private fun updateState(newState: State) {
        _state.value = newState
    }

    private fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}