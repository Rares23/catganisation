package com.catganisation.data.models

import com.squareup.moshi.Json

data class BreedImage(
    @field: Json(name="id") val id: String,
    @field: Json(name="url") val url: String)