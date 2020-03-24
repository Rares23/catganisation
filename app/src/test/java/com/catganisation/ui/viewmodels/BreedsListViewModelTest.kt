package com.catganisation.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.AuthRepository
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.FilterRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class BreedsListViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var breedsRepository: BreedRepository
    @Mock
    private lateinit var filterRepository: FilterRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var breedsListViewModel: BreedsListViewModel

    private lateinit var isLoading: LiveData<Boolean>
    private lateinit var breedsList: LiveData<List<Breed>>

    private val seed: List<Breed> = listOf(
        Breed("1", "A", "abc", "RO", "x", "wiki.com", null),
        Breed("2", "B", "def", "FR", "y", "wiki.com", null),
        Breed("3", "C", "ghi", "EN", "z", "wiki.com", null)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        breedsListViewModel = BreedsListViewModel(
            breedsRepository,
            filterRepository,
            authRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline())

        isLoading = breedsListViewModel.loading
        breedsList = breedsListViewModel.breedsList

    }

    @After
    fun tearDown() {}

    @Test
    fun `test loading state before and after getBreeds method call`() {
        Mockito.`when`(filterRepository.getActiveFilters())
            .thenReturn(Observable.just(HashSet()))

        Mockito.`when`(breedsRepository.getBreeds(HashSet()))
            .thenReturn(Observable.just(seed))

        var isLoading = this.isLoading.value
        assertNotNull(isLoading)
        isLoading?.let {
            assertTrue(it)
        }

        breedsListViewModel.getBreeds()
        verify(breedsRepository).getBreeds(HashSet())
        isLoading = this.isLoading.value
        assertNotNull(isLoading)
        isLoading?.let {
            assertFalse(it)
        }
    }

    @Test
    fun `test return breeds lists after getBreeds method call`() {
        Mockito.`when`(filterRepository.getActiveFilters())
            .thenReturn(Observable.just(HashSet()))

        Mockito.`when`(breedsRepository.getBreeds(HashSet()))
            .thenReturn(Observable.just(seed))

        val expected: List<Breed> = listOf(
            Breed("1", "A", "abc", "RO", "x", "wiki.com", null),
            Breed("2", "B", "def", "FR", "y", "wiki.com", null),
            Breed("3", "C", "ghi", "EN", "z", "wiki.com", null)
        )

        breedsListViewModel.getBreeds()

        assertNotNull(breedsList.value)
        assertTrue(expected.size == breedsList.value?.size)
        breedsList.value?.let {
            for ((i, breed) in it.withIndex()) {
                assertTrue(expected[i].id == breed.id)
            }
        }
    }
}