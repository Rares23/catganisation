package com.catganisation.data.models

import com.catganisation.data.utils.AuthConstants
import com.squareup.moshi.Json

data class User(
    @field:Json(name = AuthConstants.USER_EMAIL) val email: String,
    @field:Json(name = AuthConstants.USER_TOKEN) val userToken: String
)