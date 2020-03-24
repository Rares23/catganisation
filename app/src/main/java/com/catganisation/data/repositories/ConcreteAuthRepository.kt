package com.catganisation.data.repositories

import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.User
import com.catganisation.data.network.AuthApiService
import com.catganisation.data.utils.AuthConstants
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val loggedUserDataSource: LoggedUserDataSource
) : AuthRepository {
    override fun login(email: String, password: String): Observable<AuthResponse> {
        return Observable.just("")
            .map {
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

                return@map AuthResponse(success, validation)
            }.map {
                return@map if(it.success) {
                    val user = authApiService.login(email, password).blockingFirst()
                    if(user != null) {
                        loggedUserDataSource.saveLoggedUser(user)
                        AuthResponse(true, null)
                    } else {
                        val validation: HashMap<String, String> = HashMap()
                        validation["error"] = "Something went wrong"
                        AuthResponse(false, validation)
                    }
                } else {
                    it
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