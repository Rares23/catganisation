package com.catganisation.data.models

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "user_token") val userToken: String
)