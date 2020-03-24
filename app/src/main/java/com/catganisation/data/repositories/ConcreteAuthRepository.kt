package com.catganisation.data.repositories

import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.User
import com.catganisation.data.network.AuthApiService
import com.catganisation.data.utils.AuthConstants
import io.reactivex.Observable
import io.reactivex.ObservableSource
import javax.inject.Inject

class ConcreteAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val loggedUserDataSource: LoggedUserDataSource
) : AuthRepository {
    override fun login(email: String, password: String): Observable<AuthResponse> {
        return Observable.just("")
            .flatMap {
                var success: Boolean = true
                val validation: HashMap<String, String> = HashMap()
                if(email.isEmpty()) {
                    success = false
                    validation[AuthConstants.USER_EMAIL] = "Please fill your email"
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