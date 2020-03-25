package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.AuthRepository
import com.catganisation.data.repositories.BreedRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BreedDetailsViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
): BaseViewModel() {
    private lateinit var subscription: Disposable
    val breed: MutableLiveData<Breed> = MutableLiveData()


    fun loadBreed(breedId: String?) {
        if(breedId == null) {
            breed.value = null
        } else {
            subscription = breedRepository.getBreedById(breedId)
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

    private fun onLoadBreedStart() {}
    private fun onLoadBreedFinish() {}
    private fun onLoadBreedSuccess(breed: Breed?) {
        this.breed.value = breed
    }

    private fun onLoadBreedError(error: Throwable) {
        error.printStackTrace()
        //show a error message
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}