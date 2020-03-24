package com.catganisation.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.datasource.CountriesDataSource
import com.catganisation.data.models.Breed
import com.catganisation.data.models.Country
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class ConcreteCountryRepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    var seed: List<Locale> = listOf(
        Locale("", "RO"),
        Locale("", "FR")
    )

    @Mock
    private lateinit var countriesDataSource: CountriesDataSource
    private lateinit var countryRepository: CountryRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(countriesDataSource.provideCountries())
            .thenReturn(seed)

        countryRepository = ConcreteCountryRepository(countriesDataSource)
    }

    @Test
    fun `test get all countries method`() {
        val testObserver: TestObserver<List<Country>> = TestObserver()
        val expected: List<Country> = listOf(
            Country("RO", "Romania"),
            Country("FR", "France")
        )

        countryRepository.getAllCountries().subscribe(testObserver)
        testObserver.assertValue(expected)
    }

    @Test
    fun `test get country by code method`() {
        val testObserver: TestObserver<Country> = TestObserver()
        val expected: Country = Country("RO", "Romania")

        countryRepository.getCountryByCode("RO").subscribe(testObserver)
        testObserver.assertValue(expected)
    }
}