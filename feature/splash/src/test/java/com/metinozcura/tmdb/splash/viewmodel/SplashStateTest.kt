package com.metinozcura.tmdb.splash.viewmodel

import com.metinozcura.tmdb.common.base.CallState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SplashStateTest {

    @Test
    fun `isLoading true when contentState is Loading`() {
        val state = SplashState(contentState = CallState.Loading)
        assertTrue(state.isLoading)
    }

    @Test
    fun `isLoading false when contentState is Success`() {
        val state = SplashState(contentState = CallState.Success(Unit))
        assertFalse(state.isLoading)
    }

    @Test
    fun `isLoading false when contentState is Error`() {
        val state = SplashState(contentState = CallState.Error("error"))
        assertFalse(state.isLoading)
    }

    @Test
    fun `errorMessage returns message when contentState is Error`() {
        val state = SplashState(contentState = CallState.Error("Config failed"))
        assertEquals("Config failed", state.errorMessage)
    }

    @Test
    fun `errorMessage null when contentState is Loading`() {
        val state = SplashState(contentState = CallState.Loading)
        assertTrue(state.errorMessage == null)
    }

    @Test
    fun `errorMessage null when contentState is Success`() {
        val state = SplashState(contentState = CallState.Success(Unit))
        assertTrue(state.errorMessage == null)
    }
}
