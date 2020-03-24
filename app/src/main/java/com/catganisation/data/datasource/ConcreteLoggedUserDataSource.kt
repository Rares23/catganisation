package com.catganisation.data.datasource

import android.content.SharedPreferences
import com.catganisation.data.models.User
import com.catganisation.data.utils.AuthConstants
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteLoggedUserDataSource @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : LoggedUserDataSource {

    override fun getLoggedUser(): Observable<User> {
        return Observable.just {
            val email: String? = sharedPrefs.getString(AuthConstants.USER_EMAIL, null)
            val userToken: String? = sharedPrefs.getString(AuthConstants.USER_TOKEN, null)

            if(email.isNullOrEmpty() || userToken.isNullOrEmpty()) {
                return@just AuthConstants.NULL_USER
            } else {
                return@just User(email, userToken)
            }
        }.map { it.invoke() }
    }

    override fun saveLoggedUser(user: User): Observable<Boolean> {
        return Observable.just {
            val email: String = user.email
            val userToken: String = user.userToken

            val commitEmail =
                sharedPrefs.edit().putString(AuthConstants.USER_EMAIL, email).commit()

            val commitUserToken =
                sharedPrefs.edit().putString(AuthConstants.USER_TOKEN, userToken).commit()

            return@just commitEmail && commitUserToken
        }.map { it.invoke() }
    }

    override fun resetLoggedUser(): Observable<Boolean> {
        return Observable.just {
            val commitEmail = sharedPrefs.edit().remove(AuthConstants.USER_EMAIL).commit()
            val commitUserToken = sharedPrefs.edit().remove(AuthConstants.USER_TOKEN).commit()

            return@just commitEmail && commitUserToken
        }.map { it.invoke() }
    }
}