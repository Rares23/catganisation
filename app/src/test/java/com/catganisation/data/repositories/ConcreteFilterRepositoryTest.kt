package com.catganisation.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.CountriesFilter
import com.catganisation.data.models.Country
import com.catganisation.data.models.Filter
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations

class ConcreteFilterRepositoryTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    var filtersSeed: HashSet<Filter<*>> = hashSetOf(
        CountriesFilter(hashSetOf(Country("RO", "Romania")))
    )

    private lateinit var filterRepository: FilterRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        filterRepository = ConcreteFilterRepository()
    }

    @Test
    fun `test get active filters method`() {
        val testObservable: TestObserver<HashSet<Filter<*>>> = TestObserver()
        val expected: HashSet<Filter<*>> = HashSet()

        filterRepository.getActiveFilters().subscribe(testObservable)
        testObservable.assertValue(expected)
    }

    @Test
    fun `test update filters method and then get active filters`() {
        val testObservable: TestObserver<HashSet<Filter<*>>> = TestObserver()
        val expected: HashSet<Filter<*>> = filtersSeed

        filterRepository.updateFilters(filtersSeed).subscribe(testObservable)
        testObservable.assertValue(expected)

        filterRepository.getActiveFilters().subscribe(testObservable)
        testObservable.assertValue(expected)
    }
}