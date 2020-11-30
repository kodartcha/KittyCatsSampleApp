package com.gabriel_codarcea.features.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.core.network.model.LoginResponse
import com.gabriel_codarcea.core.network.model.User
import com.gabriel_codarcea.core.network.service.LoginService
import com.gabriel_codarcea.features.login.helpers.testModule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import retrofit2.mock.Calls
import java.io.IOException

class LoginViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var loginService: LoginService

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        startKoin { modules(testModule) }
    }

    @Test
    fun `login should return error state`() {
        every { loginService.doLogin(any(), any()) } returns Calls.failure(IOException())

        val loginViewModel = LoginViewModel()

        loginViewModel.startLogin()

        assertEquals(loginViewModel.loginState.value, LoadingState.ERROR)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `login should return in progress state`() {
        every { loginService.doLogin(any(), any()) } returns Calls.response(getFakeLoginResponse())
        val loginViewModel = LoginViewModel()
        val mockObserver: Observer<LoadingState> =
            mockkClass(Observer::class) as Observer<LoadingState>
        every { mockObserver.onChanged(LoadingState.EMPTY) } just Runs
        every { mockObserver.onChanged(LoadingState.IN_PROGRESS) } just Runs
        every { mockObserver.onChanged(LoadingState.ERROR) } just Runs
        every { mockObserver.onChanged(LoadingState.FINISHED) } just Runs
        loginViewModel.state().observeForever(mockObserver)

        loginViewModel.startLogin()

        verify { mockObserver.onChanged(LoadingState.IN_PROGRESS) }
    }

    private fun getFakeLoginResponse(): LoginResponse = LoginResponse(
        0,
        "token",
        "refresh",
        User(
            0,
            "first",
            "last",
            "email"
        )
    )
}