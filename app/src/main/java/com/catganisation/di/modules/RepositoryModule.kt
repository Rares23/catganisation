package com.catganisation.di.modules

import com.catganisation.data.datasource.CountriesDataSource
import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.network.AuthApiService
import com.catganisation.data.network.BreedApiService
import com.catganisation.data.repositories.*
import dagger.Module
import dagger.Provides
import java.util.regex.Pattern
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {
    companion object {
        const val EMAIL_ADDRESS_PATTERN: String = "email_address_pattern"
    }

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

    @Provides
    @Singleton
    fun provideFiltersRepository() : FilterRepository {
        return ConcreteFilterRepository()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiService: AuthApiService,
                              loggedUserDataSource: LoggedUserDataSource,
                              @Named(EMAIL_ADDRESS_PATTERN)emailAddressPattern: Pattern
    ): AuthRepository {
        return ConcreteAuthRepository(authApiService, loggedUserDataSource, emailAddressPattern)
    }
}