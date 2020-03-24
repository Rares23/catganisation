package com.catganisation.data.utils

import com.catganisation.data.models.User

object AuthConstants {
    const val LOGGED_USER_EMAIL: String = "user_email"
    const val LOGGED_USER_TOKEN: String = "user_token"

    val NULL_USER: User = User("", "")
}