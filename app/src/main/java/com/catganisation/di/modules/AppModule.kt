package com.catganisation.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.catganisation.app.CatganisationApplication
import com.catganisation.data.datasource.ConcreteCountriesDataSource
import com.catganisation.data.datasource.ConcreteLoggedUserDataSource
import com.catganisation.data.datasource.CountriesDataSource
import com.catganisation.data.datasource.LoggedUserDataSource
import com.catganisation.data.network.AuthApiService
import com.catganisation.data.network.BreedApiService
import com.catganisation.data.network.FakeAuthApiService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    RepositoryModule::class,
    SchedulerModule::class
])
class AppModule {
    @Provides
    @Singleton
    fun provideBreedApiService() : BreedApiService = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(BreedApiService::class.java)

    @Provides
    fun provideApplication() : Application = CatganisationApplication.app

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context = application.applicationContext

    @Provides
    @Singleton
    fun providesResources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context) : SharedPreferences = context.getSharedPreferences("catsharedprefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideConcreteCountriesDataSource() : CountriesDataSource = ConcreteCountriesDataSource()

    @Provides
    @Singleton
    fun provideConcreteLoggedUserDataSource(sharedPrefs: SharedPreferences) : LoggedUserDataSource = ConcreteLoggedUserDataSource(sharedPrefs)

    @Provides
    @Singleton
    fun provideAuthApiService() : AuthApiService = FakeAuthApiService()
}