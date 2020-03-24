package com.catganisation.data.datasource

import com.catganisation.data.models.User
import io.reactivex.Observable


interface LoggedUserDataSource {
    fun getLoggedUser() : Observable<User>
    fun saveLoggedUser(user: User) : Observable<Boolean>
    fun resetLoggedUser() : Observable<Boolean>
}