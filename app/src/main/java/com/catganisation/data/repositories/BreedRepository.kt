package com.catganisation.data.repositories

import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import io.reactivex.Observable

interface BreedRepository {
    fun getBreeds() : Observable<List<Breed>>
}