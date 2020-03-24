package com.catganisation.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.datasource.LoggedUserDataSource
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
import org.mockito.MockitoAnnotations
import java.util.*

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

    private lateinit var authRepository : AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        authRepository = ConcreteAuthRepository(
            authApiService,
            loggedUserDataSource
        )
    }

    @Test
    fun `test login successfully`() {
        Mockito.`when`(authApiService.login("test", "catlord"))
            .thenReturn(Observable.just(User("test", "cat_lord_token")))

        val testObserver: TestObserver<User> = TestObserver()
        authRepository.login("test", "catlord").subscribe(testObserver)

        testObserver.assertValue {
            it.email == "test" && it.userToken == "cat_lord_token"
        }
    }

    @Test
    fun `test login with validation error`() {
        Mockito.`when`(loggedUserDataSource.saveLoggedUser(User("test", "wrongpassword")))
            .thenReturn(Observable.just(true))

        Mockito.`when`(authApiService.login("test", "wrongpassword"))
            .thenReturn(Observable.error(Error("The username or password is incorrect")))


        val testObserver: TestObserver<User> = TestObserver()
        authRepository.login("test", "wrongpassword").subscribe(testObserver)
        testObserver.assertError(Error::class.java)
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