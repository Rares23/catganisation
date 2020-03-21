package com.catganisation.data.network

import com.catganisation.data.models.Breed
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("x-api-key: DEMO-API-KEY")
    @GET("breeds")
    fun fetchBreeds() : Observable<List<Breed>>
}