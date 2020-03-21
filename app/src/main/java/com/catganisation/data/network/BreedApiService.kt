package com.catganisation.data.network

import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import io.reactivex.Observable
import retrofit2.http.*

interface BreedService {
    @Headers("x-api-key: DEMO-API-KEY")
    @GET("breeds")
    fun fetchBreeds() : Observable<List<Breed>>

    @Headers("x-api-key: DEMO-API-KEY")
    @GET("images/search")
    fun fetchBreedImages(@Query("breed_id") breedId: String) : Observable<List<BreedImage>>
}