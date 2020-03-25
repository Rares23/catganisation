package com.catganisation.data.repositories

import android.util.Patterns
import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.User
import com.catganisation.data.network.AuthApiService
import com.catganisation.data.utils.AuthConstants
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ConcreteAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val emailAddressPattern: Pattern
) : AuthRepository {
    override fun login(email: String, password: String): Observable<AuthResponse> {
        return Observable.just("")
            .flatMap {
                var success: Boolean = true
                val validation: HashMap<String, String> = HashMap()

                if(email.isEmpty()) {
                    success = false
                    validation[AuthConstants.USER_EMAIL] = "Please fill your email"
                } else if(!emailAddressPattern.matcher(email).matches()) {
                    success = false
                    validation[AuthConstants.USER_EMAIL] = "The email is invalid"
                }

                if(password.isEmpty()) {
                    success = false
                    validation[AuthConstants.USER_PASSWORD] = "Please fill you password"
                }

                return@flatMap Observable.just(AuthResponse(success, validation))
            }.flatMap {response ->
                if(response.success) {
                    return@flatMap authApiService.login(email, password).flatMap {user ->
                        loggedUserDataSource.saveLoggedUser(user).flatMap {
                            Observable.just(AuthResponse(it, null))
                        }
                    }
                } else {
                    return@flatMap Observable.just(response)
                }
            }
    }

    override fun logout(): Observable<Boolean> {
        return loggedUserDataSource.resetLoggedUser()
    }

    override fun getLoggedUser(): Observable<User> {
        return loggedUserDataSource.getLoggedUser()
    }
}