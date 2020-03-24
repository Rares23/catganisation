package com.catganisation.data.repositories

import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.models.User
import com.catganisation.data.network.AuthApiService
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val loggedUserDataSource: LoggedUserDataSource
) : AuthRepository {
    override fun login(email: String, password: String): Observable<User?> {
        return authApiService.login(email, password)
            .map {
                loggedUserDataSource.saveLoggedUser(it)
                return@map it
            }
    }

    override fun logout(): Observable<Boolean> {
        return loggedUserDataSource.resetLoggedUser()
    }

    override fun getLoggedUser(): Observable<User> {
        return loggedUserDataSource.getLoggedUser()
    }
}