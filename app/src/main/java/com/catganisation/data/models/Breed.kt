package com.catganisation.data.models

import com.squareup.moshi.Json


data class Breed(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "description")val description: String,
    @field:Json(name = "country_code")val countryCode: String,
    @field:Json(name = "temperament")val temperament: String,
    @field:Json(name = "wikipedia_url")val wikiLink: String,
    var imageUrl: String?
) {

}