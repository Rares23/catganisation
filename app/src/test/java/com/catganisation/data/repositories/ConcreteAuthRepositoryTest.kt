package com.catganisation.data.repositories

import android.util.Patterns
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.User
import com.catganisation.data.network.AuthApiService
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class ConcreteAuthRepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var authApiService: AuthApiService
    @Mock
    private lateinit var loggedUserDataSource: LoggedUserDataSource

    @Mock
    private lateinit var emailAddressPattern: Pattern

    @Mock
    private lateinit var emailAddressMatcher: Matcher

    private lateinit var authRepository : AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        authRepository = ConcreteAuthRepository(
            authApiService,
            loggedUserDataSource,
            emailAddressPattern
        )
    }

    @Test
    fun `test login successfully`() {
        val user: User = User("test@cats.com", "cat_lord_token")

        Mockito.`when`(emailAddressPattern.matcher("test@cats.com"))
            .thenReturn(emailAddressMatcher)

        Mockito.`when`(emailAddressMatcher.matches())
            .thenReturn(true)

        Mockito.`when`(loggedUserDataSource.saveLoggedUser(user))
            .thenReturn(Observable.just(true))

        Mockito.`when`(authApiService.login("test@cats.com", "catlord"))
            .thenReturn(Observable.just(user))

        val testObserver: TestObserver<AuthResponse> = TestObserver()
        authRepository.login("test@cats.com", "catlord").subscribe(testObserver)

        testObserver.assertValue {
            it.success
        }
    }

    @Test
    fun `test login with validation error`() {
        Mockito.`when`(emailAddressPattern.matcher("test"))
            .thenReturn(emailAddressMatcher)

        Mockito.`when`(emailAddressMatcher.matches())
            .thenReturn(false)

        Mockito.`when`(loggedUserDataSource.saveLoggedUser(User("test@cats.com", "wrongpassword")))
            .thenReturn(Observable.just(true))

        Mockito.`when`(authApiService.login("test@cats.com", "wrongpassword"))
            .thenReturn(Observable.error(Error("The username or password is incorrect")))


        val testObserver: TestObserver<AuthResponse> = TestObserver()
        authRepository.login("test", "wrongpassword").subscribe(testObserver)
        testObserver.assertValue {
            !it.success
        }
    }

    @Test
    fun `test logout`() {
        Mockito.`when`(loggedUserDataSource.resetLoggedUser())
            .thenReturn(Observable.just(true))

        val testObserver: TestObserver<Boolean> = TestObserver()
        authRepository.logout().subscribe(testObserver)
        testObserver.assertResult(true)
    }

    @Test
    fun `test get logged user`() {
        Mockito.`when`(loggedUserDataSource.getLoggedUser())
            .thenReturn(Observable.just(User("test", "cat_lord_token")))

        val testObserver: TestObserver<User> = TestObserver()

        authRepository.getLoggedUser().subscribe(testObserver)
        testObserver.assertValue {
            it.email == "test" && it.userToken == "cat_lord_token"
        }

    }
}