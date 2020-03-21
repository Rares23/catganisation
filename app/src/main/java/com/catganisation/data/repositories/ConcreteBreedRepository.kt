package com.catganisation.data.repositories

import com.catganisation.data.models.Breed
import com.catganisation.data.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteBreedRepository @Inject constructor(private val breedService: ApiService) : BreedRepository {

    private var cachedBreeds = emptyList<Breed>()

    override fun fetchBreeds(): Observable<List<Breed>> {
        return if(cachedBreeds.isEmpty()) {
            breedService.fetchBreeds()
                .doOnNext { cachedBreeds = it }
        } else {
            Observable.just(cachedBreeds)
                .mergeWith(breedService.fetchBreeds())
                .doOnNext { cachedBreeds = it }
        }
    }
}