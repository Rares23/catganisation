package com.catganisation.data.network

import com.catganisation.data.models.User
import io.reactivex.Observable
import retrofit2.Retrofit

class FakeAuthApiService : AuthApiService {
    override fun login(email: String, password: String): Observable<User> {
        return Observable
            .just {
                return@just if(email == "admin" && password == "catlord") {
                    User(email, "cat_lord_token")
                } else {
                    throw Error("The username or password is incorrect!")
                }
            }.map { it.invoke() }

    }
}