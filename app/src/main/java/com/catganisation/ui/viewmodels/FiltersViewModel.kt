package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.actions.NotifyAction
import com.catganisation.data.actions.StringNotifyAction
import com.catganisation.data.models.CountriesFilter
import com.catganisation.data.models.Country
import com.catganisation.data.models.Filter
import com.catganisation.data.repositories.CountryRepository
import com.catganisation.data.repositories.FilterRepository
import com.catganisation.data.utils.FiltersTags
import com.catganisation.data.utils.Selectable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FiltersViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    private val filterRepository: FilterRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseViewModel() {
    
    private lateinit var loadCountriesSubscription: Disposable
    private lateinit var loadActiveFilters: Disposable
    private lateinit var saveFilters: Disposable
    private lateinit var resetFilters: Disposable

    private var countriesList: MutableList<Selectable<Country>> = mutableListOf()
    val countries: MutableLiveData<List<Selectable<Country>>> = MutableLiveData()

    val countryNotifyAction: MutableLiveData<StringNotifyAction?> = MutableLiveData()
    val submitNotify: MutableLiveData<Boolean?> = MutableLiveData()

    fun loadFiltersData() {
        loadCountriesSubscription = countryRepository.getAllCountries()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnComplete {
                loadActiveFilters()
            }
            .subscribe{ countries ->
                this.countriesList.clear()
                countries.forEach {
                    this.countriesList.add(Selectable(it.clone() as Country, false))
                }
            }
    }

    private fun loadActiveFilters() {
        loadActiveFilters = filterRepository.getActiveFilters()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                it.forEach {filter ->
                    when(filter.getTag()) {
                        FiltersTags.COUNTRIES_FILTER_TAG -> {
                            setCountriesSelectStateFromFilters(filter as CountriesFilter)
                        }
                    }
                }

                this.countries.value = countriesList
            }
    }

    private fun setCountriesSelectStateFromFilters(filter: CountriesFilter) {
        filter.value.forEach {selectedCountry ->
            countriesList.forEach { country ->
                if(country.value.code == selectedCountry.code) {
                    country.selected = true
                }
            }
        }
    }

    fun selectCountry(country: Selectable<Country>) {
        country.selected = !country.selected
        countryNotifyAction.postValue(StringNotifyAction(
            NotifyAction.Action.UPDATE,
            country.value.code
        ))
    }

    fun saveFilters() {
        val newFilters: HashSet<Filter<*>> = HashSet()

        setFiltersData(newFilters)

        saveFilters = filterRepository.updateFilters(newFilters)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                submitNotify.value = true
            }

    }

    private fun setFiltersData(filters: HashSet<Filter<*>>) {
        val selectedCountries: HashSet<Country> = HashSet()
        countriesList.forEach {
            if(it.selected) {
                selectedCountries.add(it.value.clone() as Country)
            }
        }

        filters.add(CountriesFilter(selectedCountries))
    }

    fun resetFilters() {
        resetFilters = filterRepository.updateFilters(HashSet())
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                submitNotify.value = true
            }
    }

    override fun onCleared() {
        super.onCleared()

        loadCountriesSubscription.dispose()
        loadActiveFilters.dispose()
        saveFilters.dispose()
        resetFilters.dispose()
    }

    fun clearData() {
        submitNotify.value = null
        countryNotifyAction.value = null
        countriesList.clear()
    }
}