package com.catganisation.data.repositories

import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.User
import io.reactivex.Observable


interface AuthRepository {
    fun login(email: String, password: String) : Observable<AuthResponse>
    fun logout() : Observable<Boolean>
    fun getLoggedUser() : Observable<User>
}