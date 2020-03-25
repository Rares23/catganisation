package com.catganisation.data.repositories

import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import com.catganisation.data.models.CountriesFilter
import com.catganisation.data.models.Filter
import com.catganisation.data.network.BreedApiService
import com.catganisation.data.utils.FiltersTags
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteBreedRepository @Inject constructor(private val breedApiService: BreedApiService) : BreedRepository {

    private var cachedBreeds: List<Breed> = emptyList()
    private var cachedImagesUrls: HashMap<String, BreedImage> = HashMap()

    override fun getBreeds(filters: HashSet<Filter<*>>): Observable<List<Breed>> {
        return fetchBreeds()
            .map {
                return@map if(filters.isEmpty()) {
                    it
                } else {
                    getFilteredBreeds(filters, it)
                }.sortedBy { breed -> breed.name }
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
        }
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

    override fun getBreedImage(breedId: String): Observable<BreedImage> {
        if(cachedImagesUrls.containsKey(breedId)) {
            return Observable.just(cachedImagesUrls[breedId])
        } else {
            return fetchBreedImages(breedId)
                .map { breedImages ->
                    if (!breedImages.isNullOrEmpty()) {
                        cachedImagesUrls[breedId] = breedImages[0]
                    }

                    return@map cachedImagesUrls[breedId]
                }
        }
    }

    private fun fetchBreedImages(breedId: String): Observable<List<BreedImage>> {
        return breedApiService.fetchBreedImages(breedId)
    }
}

