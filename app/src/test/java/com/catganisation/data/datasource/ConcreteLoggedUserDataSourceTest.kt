package com.catganisation.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.User
import com.catganisation.data.utils.AuthConstants
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class ConcreteLoggedUserDataSourceTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var loggedUserDataSource: LoggedUserDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)

        loggedUserDataSource = ConcreteLoggedUserDataSource(sharedPreferences)
    }

    @Test
    fun `test get logged user data if not logged`() {
        Mockito.`when`(sharedPreferences.getString(AuthConstants.LOGGED_USER_EMAIL, null))
            .thenReturn(null)

        Mockito.`when`(sharedPreferences.getString(AuthConstants.LOGGED_USER_TOKEN, null))
            .thenReturn(null)

        val testObserver: TestObserver<User?> = TestObserver()
        val expected: User? = AuthConstants.NULL_USER

        loggedUserDataSource.getLoggedUser().subscribe(testObserver)
        testObserver.assertValue(expected)

    }

    @Test
    fun `test get logged user when there is data`() {
        Mockito.`when`(sharedPreferences.getString(AuthConstants.LOGGED_USER_EMAIL, null))
            .thenReturn("test")

        Mockito.`when`(sharedPreferences.getString(AuthConstants.LOGGED_USER_TOKEN, null))
            .thenReturn("asd")

        val testObserver: TestObserver<User?> = TestObserver()
        val expected: User = User("test", "asd")

        loggedUserDataSource.getLoggedUser().subscribe(testObserver)
        testObserver.assertValue {
            it.email == expected.email && it.userToken == expected.userToken
        }
    }


    @Test
    fun saveLoggedUser() {
        Mockito.`when`(editor.putString(AuthConstants.LOGGED_USER_EMAIL, "test"))
            .thenReturn(editor)

        Mockito.`when`(editor.putString(AuthConstants.LOGGED_USER_TOKEN, "asd"))
            .thenReturn(editor)

        Mockito.`when`(sharedPreferences.edit().putString(AuthConstants.LOGGED_USER_EMAIL, "test").commit())
            .thenReturn(true)

        Mockito.`when`(sharedPreferences.edit().putString(AuthConstants.LOGGED_USER_TOKEN, "asd").commit())
            .thenReturn(true)

        val testObserver: TestObserver<Boolean> = TestObserver()
        val expected: Boolean = true
        loggedUserDataSource.saveLoggedUser(User("test", "asd")).subscribe(testObserver)
        testObserver.assertValue(expected)
    }

    @Test
    fun resetLoggedUser() {
        Mockito.`when`(editor.remove(AuthConstants.LOGGED_USER_EMAIL))
            .thenReturn(editor)

        Mockito.`when`(editor.remove(AuthConstants.LOGGED_USER_TOKEN))
            .thenReturn(editor)

        Mockito.`when`(editor.remove(AuthConstants.LOGGED_USER_EMAIL).commit())
            .thenReturn(true)

        Mockito.`when`(editor.remove(AuthConstants.LOGGED_USER_TOKEN).commit())
            .thenReturn(true)

        val testObserver: TestObserver<Boolean> = TestObserver()
        loggedUserDataSource.resetLoggedUser().subscribe(testObserver)
        testObserver.assertResult(true)
    }
}