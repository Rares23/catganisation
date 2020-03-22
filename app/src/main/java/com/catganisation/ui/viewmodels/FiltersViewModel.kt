package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.Country
import com.catganisation.data.repositories.CountryRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FiltersViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseViewModel() {

    private lateinit var subscription: Disposable

    var countries: MutableLiveData<List<Country>> = MutableLiveData()
    var selectedCodes: MutableLiveData<List<String>> = MutableLiveData()

    init {
        selectedCodes.value = emptyList()
    }

    fun loadCountries() {
        subscription = countryRepository.getAllCountries()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnSubscribe {
                onFetchCountriesStart()
            }
            .doOnTerminate {
                onFetchCountriesFinish()
            }
            .subscribe(
                {breeds ->
                    onFetchCountriesSuccess(breeds)
                },
                {error ->
                    onFetchCountriesError(error)
                }
            )
    }

    private fun onFetchCountriesStart() {}

    private fun onFetchCountriesFinish() {}

    private fun onFetchCountriesSuccess(countries: List<Country>) {
        this.countries.value = countries
    }

    private fun onFetchCountriesError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}