package com.catganisation.di.modules

import com.catganisation.data.network.ApiService
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.ConcreteBreedRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideConcreteBreedRepository(apiService: ApiService) : BreedRepository {
        return ConcreteBreedRepository(apiService)
    }
}