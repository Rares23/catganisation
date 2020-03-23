package com.catganisation.data.repositories

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import com.catganisation.data.models.CountriesFilter
import com.catganisation.data.models.Filter
import com.catganisation.data.network.BreedApiService
import com.catganisation.data.utils.FiltersTags
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConcreteBreedRepository @Inject constructor(private val breedApiService: BreedApiService) : BreedRepository {

    private var cachedBreeds: List<Breed> = emptyList()
    private var cachedImagesUrls: HashMap<String, String> = HashMap()

    private lateinit var fetchBreedImagesSubscriber: Disposable

    override fun getBreeds(filters: HashSet<Filter<*>>, breedItemNotify: MutableLiveData<Breed>): Observable<List<Breed>> {
        return fetchBreeds()
            .map {
                return@map if(filters.isEmpty()) {
                    it
                } else {
                    getFilteredBreeds(filters, it)
                }.sortedBy { breed -> breed.name }
            }
            .map {
                it.forEach {breed ->
                    if(breed.imageUrl.isNullOrEmpty()) {
                        val cachedImageUrl: String? = cachedImagesUrls[breed.id]
                        if(cachedImageUrl.isNullOrEmpty()) {
                            fetchBreedImagesSubscriber = fetchBreedImages(breed.id)
                                .doOnTerminate {
                                }
                                .subscribe { breedImages ->
                                    if(!breedImages.isNullOrEmpty()) {
                                        val breedImageUrl = breedImages[0].url
                                        cachedImagesUrls[breed.id] = breedImageUrl
                                        breed.imageUrl = breedImageUrl
                                        breedItemNotify.postValue(breed)
                                    }
                                }
                        }
                        else {
                            breed.imageUrl = cachedImageUrl
                            breedItemNotify.postValue(breed)
                        }
                    }
                }

                return@map it
            }
    }

    private fun getFilteredBreeds(filters: HashSet<Filter<*>>, totalBreeds: List<Breed>) : List<Breed> {
        val filteredList: ArrayList<Breed> = ArrayList()
        filters.forEach { filter ->
            when(filter.getTag()) {
                FiltersTags.COUNTRIES_FILTER_TAG -> {
                    if(filter is CountriesFilter) {
                        filter.value.forEach {country ->
                            totalBreeds.forEach {breed ->
                                if(breed.countryCode == country.code) {
                                    filteredList.add(breed)
                                }
                            }
                        }
                    }
                }
            }
        }

        return filteredList.toList()
    }

    private fun fetchBreeds(): Observable<List<Breed>> {
        return if (cachedBreeds.isEmpty()) {
            breedApiService.fetchBreeds()
                .map {
                    cachedBreeds = it
                    return@map cachedBreeds
                }
        } else {
            Observable.just(cachedBreeds)
                .mergeWith(breedApiService.fetchBreeds())
                .map {
                    cachedBreeds = it
                    return@map cachedBreeds
                }
        }
    }

    private fun fetchBreedImages(breedId: String): Observable<List<BreedImage>> {
        return breedApiService.fetchBreedImages(breedId)
    }

    override fun getBreedById(breedId: String): Observable<Breed?> {
        return Observable.just(cachedBreeds)
            .map { breeds ->
                breeds.forEach {breed ->
                    if(breed.id == breedId) {
                        return@map breed
                    }
                }

                return@map null
            }
    }
}

