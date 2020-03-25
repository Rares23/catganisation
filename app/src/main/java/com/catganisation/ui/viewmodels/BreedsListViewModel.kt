package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.AuthRepository
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.FilterRepository
import com.catganisation.data.utils.AuthConstants
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BreedsListViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val filterRepository: FilterRepository,
    private val authRepository: AuthRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler) : BaseViewModel() {

    private lateinit var loadBreedsSubscription: Disposable
    private lateinit var checkLoggedUserSubscription: Disposable
    private val loadBreedImageDisposables: HashMap<String, Disposable> = HashMap()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val breedsList: MutableLiveData<List<Breed>> = MutableLiveData()
    val isLogged: MutableLiveData<Boolean> = MutableLiveData()

    val notifyBreedItemUpdate: MutableLiveData<Breed> = MutableLiveData()

    init {
        loading.value = true
        breedsList.value = emptyList()
    }

    fun getBreeds() {
        loadBreedsSubscription = filterRepository.getActiveFilters()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .flatMap { filters ->
                breedRepository.getBreeds(filters)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doOnSubscribe { onFetchBreedsListStart() }
                    .doOnTerminate {
                        onFetchBreedsListFinish()
                    }
            }.subscribe(
                { onFetchBreedsListSuccess(it) },
                { onFetchBreedsListError(it) }
            )

    }

    fun getBreedImage(breed: Breed) {
        loadBreedImageDisposables[breed.id] = breedRepository.getBreedImage(breed.id)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnTerminate {
                loadBreedImageDisposables[breed.id]?.dispose()
                loadBreedImageDisposables.remove(breed.id)
            }
            .subscribe {
                breed.imageUrl = it.url
                notifyBreedItemUpdate.value = breed
            }
    }

    private fun onFetchBreedsListStart() {
        loading.value = true
    }
    private fun onFetchBreedsListFinish() {
        loading.value = false
    }
    private fun onFetchBreedsListSuccess(breeds: List<Breed>) {
        this.breedsList.value = breeds
    }

    private fun onFetchBreedsListError(error: Throwable) {
        error.printStackTrace()
        //show a error message
    }

    fun checkLoggedUser() {
        checkLoggedUserSubscription = authRepository.getLoggedUser()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe (
                {
                    isLogged.value = it != AuthConstants.NULL_USER
                },
                {
                    isLogged.value = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        loadBreedsSubscription.dispose()
        checkLoggedUserSubscription.dispose()
    }
}