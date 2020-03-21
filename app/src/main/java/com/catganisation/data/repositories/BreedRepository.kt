package com.catganisation.data.repositories

import com.catganisation.data.models.Breed
import io.reactivex.Observable

interface BreedRepository {
    fun fetchBreeds() : Observable<List<Breed>>
}