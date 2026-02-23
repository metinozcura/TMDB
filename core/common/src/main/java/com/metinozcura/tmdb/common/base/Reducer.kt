package com.metinozcura.tmdb.common.base

interface Reducer<Intent : Reducer.Intent, State : Reducer.State, Effect : Reducer.Effect> {
    interface Intent
    interface State
    interface Effect

    fun reduce(state: State, intent: Intent): Pair<State, Effect?>
}