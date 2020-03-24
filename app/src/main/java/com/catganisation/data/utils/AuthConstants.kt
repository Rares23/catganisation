package com.catganisation.data.utils

import com.catganisation.data.models.User

object AuthConstants {
    const val USER_EMAIL: String = "email"
    const val USER_TOKEN: String = "user_token"
    const val USER_PASSWORD: String = "password"

    val NULL_USER: User = User("", "")
}