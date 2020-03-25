package com.catganisation.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.CountriesFilter
import com.catganisation.data.models.Country
import com.catganisation.data.models.Filter
import com.catganisation.data.repositories.CountryRepository
import com.catganisation.data.repositories.FilterRepository
import com.catganisation.data.utils.Selectable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FiltersViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    private var filtersWithContentSeed: HashSet<Filter<*>> = hashSetOf(
        CountriesFilter(hashSetOf(Country("RO", "Romania")))
    )

    private val countriesSeed: List<Country> = listOf(
        Country("RO", "Romania")
    )

    @Mock
    private lateinit var countryRepository: CountryRepository
    @Mock
    private lateinit var filterRepository: FilterRepository

    private lateinit var filtersViewModel: FiltersViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        filtersViewModel = FiltersViewModel(
            countryRepository,
            filterRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun loadFiltersData() {
        Mockito.`when`(countryRepository.getAllCountries())
            .thenReturn(Observable.just(countriesSeed))

        Mockito.`when`(filterRepository.getActiveFilters())
            .thenReturn(Observable.just(filtersWithContentSeed))

        filtersViewModel.loadFiltersData()

        assertNotNull(filtersViewModel.countries.value)
        assertEquals(1, filtersViewModel.countries.value?.size)
        assertEquals("RO", filtersViewModel.countries.value?.get(0)?.value?.code)
        assertEquals(true, filtersViewModel.countries.value?.get(0)?.selected)
    }

    @Test
    fun selectCountry() {
        val expected: Selectable<Country> = Selectable(Country("RO", "Romania"), true)

        val value = Selectable(Country("RO", "Romania"), false)
        filtersViewModel.selectCountry(value)
        assertEquals(expected.value.code, filtersViewModel.countryNotifyAction.value?.value)
        assertEquals(expected.selected, value.selected)
    }

    @Test
    fun saveFilters() {
        Mockito.`when`(filterRepository.updateFilters(HashSet()))
            .thenReturn(Observable.just(HashSet()))

        val expected: Boolean = true
        assertEquals(null, filtersViewModel.submitNotify.value)
        filtersViewModel.saveFilters()
        assertEquals(expected, filtersViewModel.submitNotify.value)
    }

    @Test
    fun resetFilters() {
        Mockito.`when`(filterRepository.updateFilters(HashSet()))
            .thenReturn(Observable.just(HashSet()))

        filtersViewModel.submitNotify.observeForever {}
        val expected: Boolean = true
        assertEquals(null, filtersViewModel.submitNotify.value)
        filtersViewModel.resetFilters()
        assertEquals(expected, filtersViewModel.submitNotify.value)
    }
}