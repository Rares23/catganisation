package com.catganisation.di.modules

import com.catganisation.data.repositories.AuthRepository
import com.catganisation.data.repositories.BreedRepository
import com.catganisation.data.repositories.CountryRepository
import com.catganisation.data.repositories.FilterRepository
import com.catganisation.ui.viewmodels.BreedDetailsViewModel
import com.catganisation.ui.viewmodels.BreedsListViewModel
import com.catganisation.ui.viewmodels.FiltersViewModel
import com.catganisation.ui.viewmodels.LoginViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideBreedsListViewModel(breedsRepository: BreedRepository,
                                   filterRepository: FilterRepository,
                                   @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                                   @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : BreedsListViewModel {
        return BreedsListViewModel(breedsRepository, filterRepository, ioScheduler, uiScheduler)
    }

    @Provides
    @Singleton
    fun provideBreedDetailsViewModel(breedsRepository: BreedRepository,
                                     @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                                     @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : BreedDetailsViewModel {
        return BreedDetailsViewModel(breedsRepository, ioScheduler, uiScheduler)
    }

    @Provides
    @Singleton
    fun provideFiltersViewModel(countriesRepository: CountryRepository,
                                filterRepository: FilterRepository,
                                @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                                @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : FiltersViewModel {
        return FiltersViewModel(countriesRepository, filterRepository, ioScheduler, uiScheduler)
    }

    @Provides
    @Singleton
    fun provideLoginViewModel(authRepository: AuthRepository,
                              @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                              @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : LoginViewModel {
        return LoginViewModel(authRepository, ioScheduler, uiScheduler)
    }
}