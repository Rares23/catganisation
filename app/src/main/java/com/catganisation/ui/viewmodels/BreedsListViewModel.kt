package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.actions.NotifyAction
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.FilterRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BreedsListViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val filterRepository: FilterRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler) : BaseViewModel() {

    private lateinit var subscription: Disposable
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val breedsList: MutableLiveData<List<Breed>> = MutableLiveData()
    val breedItemNotifier: MutableLiveData<Breed> = MutableLiveData()

    init {
        loading.value = true
        breedsList.value = emptyList()
    }

    fun getBreeds() {
        subscription = filterRepository.getActiveFilters()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {filters ->
                breedRepository.getBreeds(filters, breedItemNotifier)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doOnSubscribe {
                        onFetchBreedsListStart()
                    }
                    .doOnTerminate {
                        onFetchBreedsListFinish()
                    }
                    .subscribe(
                        { onFetchBreedsListSuccess(it) },
                        { onFetchBreedsListError(it) }
                    )

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

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}