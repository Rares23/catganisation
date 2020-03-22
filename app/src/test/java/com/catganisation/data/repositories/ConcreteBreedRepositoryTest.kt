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
        Breed("a", "Axi", "abc", "RO", "x", "wiki.com", null),
        Breed("c", "Cxi", "dfe", "FR", "y", "wiki.com", null),
        Breed("b", "Bxi", "xyz", "EN", "x", "wiki.com", null)
    )

    var aBreedImagesListSeed: List<BreedImage> = listOf(
        BreedImage("1a", "1a.png"),
        BreedImage("2a", "2a.png")
    )

    var bBreedImagesListSeed: List<BreedImage> = listOf(
        BreedImage("1b", "1b.png"),
        BreedImage("2b", "2b.png")
    )

    var cBreedImagesListSeed: List<BreedImage> = listOf(
        BreedImage("1c", "1c.png"),
        BreedImage("2c", "2c.png")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        breedRepository = ConcreteBreedRepository(breedsApiService)

        Mockito.`when`(breedsApiService.fetchBreeds())
            .thenReturn(Observable.just(breedListSeed))

        Mockito.`when`(breedsApiService.fetchBreedImages("a"))
            .thenReturn(Observable
                .just(aBreedImagesListSeed)
                .delay(1, TimeUnit.SECONDS))

        Mockito.`when`(breedsApiService.fetchBreedImages("b"))
            .thenReturn(Observable
                .just(bBreedImagesListSeed)
                .delay(1, TimeUnit.SECONDS))

        Mockito.`when`(breedsApiService.fetchBreedImages("c"))
            .thenReturn(Observable
                .just(cBreedImagesListSeed)
                .delay(1, TimeUnit.SECONDS))
    }

    @After
    fun tearDown() {}

    @Test
    fun `test get breeds list method with correct breed images`() {
        val testObserver: TestObserver<List<Breed>> = TestObserver()
        val expected: List<Breed> = listOf(
            Breed("a", "Axi", "abc", "RO", "x", "wiki.com", "1a.png"),
            Breed("b", "Bxi", "xyz", "EN", "x", "wiki.com", "1b.png"),
            Breed("c", "Cxi", "dfe", "FR", "y", "wiki.com", "1c.png")
        )

        breedRepository.getBreeds().subscribe(testObserver)
        testObserver.assertValue(expected)
    }
}