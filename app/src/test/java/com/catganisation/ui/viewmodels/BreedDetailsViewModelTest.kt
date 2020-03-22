package com.catganisation.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.Breed
import com.catganisation.data.repositories.BreedRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class BreedDetailsViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var breedsRepository: BreedRepository

    private lateinit var breed: LiveData<Breed>
    private lateinit var breedDetailsViewMode: BreedDetailsViewModel

    private val seed: Breed = Breed("1", "A", "abc", "RO", "x", "wiki.com", null)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        breedDetailsViewMode = BreedDetailsViewModel(breedsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline())

        breed = breedDetailsViewMode.breed
    }

    @Test
    fun `test load breed method`() {
        val breedId: String = "1"
        val expected: Breed = Breed("1", "A", "abc", "RO", "x", "wiki.com", null)

        Mockito.`when`(breedsRepository.getBreedById("1"))
            .thenReturn(Observable.just(seed))

        breedDetailsViewMode.loadBreed(breedId)
        assertNotNull(breed.value)
        assertTrue(expected.id == breed.value?.id)
    }
}