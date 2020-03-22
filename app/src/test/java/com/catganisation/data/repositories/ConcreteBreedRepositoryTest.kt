package com.catganisation.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catganisation.RxImmediateSchedulerRule
import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import com.catganisation.data.network.BreedApiService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit
import kotlin.math.exp

class ConcreteBreedRepositoryTest {
    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var breedsApiService: BreedApiService
    private lateinit var breedRepository: BreedRepository

    var breedListSeed: List<Breed> = listOf(
        Breed("1", "A", "abc", "RO", "x", "wiki.com", null)
    )

    var breedImagesListSeed: List<BreedImage> = listOf(
        BreedImage("1A", "1A.png"),
        BreedImage("2A", "2A.png")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        breedRepository = ConcreteBreedRepository(breedsApiService)
    }

    @After
    fun tearDown() {}

    @Test
    fun `get breeds list with correct breed images`() {
        Mockito.`when`(breedsApiService.fetchBreeds())
            .thenReturn(Observable.just(breedListSeed))

        Mockito.`when`(breedsApiService.fetchBreedImages("1"))
            .thenReturn(Observable
                .just(breedImagesListSeed)
                .delay(5, TimeUnit.SECONDS))

        val testObserver: TestObserver<List<Breed>> = TestObserver()
        val expected: List<Breed> = listOf(
            Breed("1", "A", "abc", "RO", "x", "wiki.com", "1A.png")
        )

        breedRepository.getBreeds().subscribe(testObserver)
        testObserver.assertValue(expected)
    }
}