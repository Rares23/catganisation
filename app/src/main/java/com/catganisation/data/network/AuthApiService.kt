package com.catganisation.data.network

import com.catganisation.data.models.User
import io.reactivex.Observable

interface AuthApiService {
    fun login(email: String, password: String) : Observable<User>
}