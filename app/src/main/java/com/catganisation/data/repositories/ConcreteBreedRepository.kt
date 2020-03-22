package com.catganisation.data.repositories

import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import com.catganisation.data.network.BreedApiService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConcreteBreedRepository @Inject constructor(private val breedApiService: BreedApiService) : BreedRepository {

    private var cachedBreeds = emptyList<Breed>()
    private var cachedImagesUrls: HashMap<String, String> = HashMap()

    override fun getBreeds(): Observable<List<Breed>> {
        return fetchBreeds()
            .map {
                it.sortedBy { breed -> breed.name }
            }
            .doOnNext {breeds ->
                breeds.forEach {breed ->
                    if(breed.imageUrl.isNullOrEmpty()) {
                        val cachedImageUrl: String? = cachedImagesUrls[breed.id]
                        if(cachedImageUrl.isNullOrEmpty()) {
                            fetchBreedImages(breed.id).subscribe {breedImages ->
                                if(!breedImages.isNullOrEmpty()) {
                                    val breedImageUrl = breedImages[0].url
                                    cachedImagesUrls[breed.id] = breedImageUrl
                                    breed.imageUrl = breedImageUrl
                                }
                            }
                        }
                        else {
                            breed.imageUrl = cachedImageUrl
                        }
                    }
                }
            }

    }

    private fun fetchBreeds(): Observable<List<Breed>> {
        return if (cachedBreeds.isEmpty()) {
            breedApiService.fetchBreeds()
                .doOnNext {
                    cachedBreeds = it
                }
        } else {
            Observable.just(cachedBreeds)
                .mergeWith(breedApiService.fetchBreeds())
                .doOnNext {
                    cachedBreeds = it
                }
        }
    }

    private fun fetchBreedImages(breedId: String): Observable<List<BreedImage>> {
        return breedApiService.fetchBreedImages(breedId)
    }
}

