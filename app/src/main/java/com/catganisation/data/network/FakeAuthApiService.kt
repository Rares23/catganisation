package com.catganisation.data.network

import com.catganisation.data.models.User
import io.reactivex.Observable
import javax.inject.Inject

class FakeAuthApiService: AuthApiService {
    override fun login(email: String, password: String): Observable<User> {
        return Observable
            .just {
                return@just if(email == "catlord@cats.com" && password == "catlord") {
                    User(email, "cat_lord_token")
                } else {
                    error("The username or password is incorrect!")
                }
            }.map { it.invoke() }

    }
}