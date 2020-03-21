package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.BreedRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BreedsListViewModel @Inject constructor(private val breedRepository: BreedRepository) : BaseViewModel() {

    private lateinit var subscription: Disposable
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val breedsList: MutableLiveData<List<Breed>> = MutableLiveData()

    fun getBreeds() {
        subscription = breedRepository.fetchBreeds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                onFetchBreedsListStart()
            }
            .doOnTerminate {
                onFetchBreedsListFinish()
            }
            .subscribe(
                {breeds ->
                    onFetchBreedsListSuccess(breeds)
                },
                {error ->
                    onFetchBreedsListError(error)
                }
            )

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