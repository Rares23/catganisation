package com.catganisation.di.modules

import com.catganisation.data.datasource.CountriesDataSource
import com.catganisation.data.network.BreedApiService
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.ConcreteBreedRepository
import com.catganisation.data.repositories.ConcreteCountryRepository
import com.catganisation.data.repositories.CountryRepository
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

    @Provides
    @Singleton
    fun provideConcreteCountryRepository(countriesDataSource: CountriesDataSource) : CountryRepository {
        return ConcreteCountryRepository(countriesDataSource)
    }
}