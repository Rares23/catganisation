package com.catganisation.di.modules

import com.catganisation.data.network.BreedApiService
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.ConcreteBreedRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideConcreteBreedRepository(breedApiService: BreedApiService) : BreedRepository {
        return ConcreteBreedRepository(breedApiService)
    }
}