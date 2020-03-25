package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.CountryRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BreedDetailsViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val countryRepository: CountryRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
): BaseViewModel() {
    private lateinit var loadBreedSubscription: Disposable
    private lateinit var loadBreedImageSubscription: Disposable
    private lateinit var loadCountrySubscription: Disposable

    val breed: MutableLiveData<Breed> = MutableLiveData()
    val country: MutableLiveData<String> = MutableLiveData()

    fun loadBreed(breedId: String?) {
        if(breedId == null) {
            breed.value = null
        } else {
            loadBreedSubscription = breedRepository.getBreedById(breedId)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnSubscribe {
                    onLoadBreedStart()
                }
                .doOnTerminate {
                    onLoadBreedFinish()
                }
                .subscribe(
                    {breed ->
                        onLoadBreedSuccess(breed)
                    },
                    {error ->
                        onLoadBreedError(error)
                    }
                )
        }
    }

    private fun loadBreedImage(breedId: String) {
        loadBreedImageSubscription = breedRepository.getBreedImage(breedId)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                breed.value?.imageUrl = it.url
            }
    }

    private fun loadCountry(countryCode: String) {
        loadCountrySubscription = countryRepository.getCountryByCode(countryCode)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                country.value = it.toString()
            }
    }

    private fun onLoadBreedStart() {}
    private fun onLoadBreedFinish() {}
    private fun onLoadBreedSuccess(breed: Breed?) {
        this.breed.value = breed

        breed?.let {
            loadBreedImage(it.id)
            loadCountry(it.countryCode)
        }
    }

    private fun onLoadBreedError(error: Throwable) {
        error.printStackTrace()
        //show a error message
    }

    override fun onCleared() {
        super.onCleared()
        if(!loadBreedSubscription.isDisposed) {
            loadBreedSubscription.dispose()
        }

        if(!loadBreedImageSubscription.isDisposed) {
            loadBreedImageSubscription.dispose()
        }

        if(!loadCountrySubscription.isDisposed) {
            loadCountrySubscription.dispose()
        }
    }
}