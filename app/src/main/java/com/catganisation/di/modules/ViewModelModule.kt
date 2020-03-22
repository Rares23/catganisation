package com.catganisation.di.modules

import com.catganisation.data.repositories.BreedRepository
import com.catganisation.ui.viewmodels.BreedDetailsViewModel
import com.catganisation.ui.viewmodels.BreedsListViewModel
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
                                   @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                                   @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : BreedsListViewModel {
        return BreedsListViewModel(breedsRepository, ioScheduler, uiScheduler)
    }

    @Provides
    @Singleton
    fun provideBreedDetailsViewModel(breedsRepository: BreedRepository,
                                     @Named(SchedulerModule.IO_SCHEDULER) ioScheduler: Scheduler,
                                     @Named(SchedulerModule.UI_SCHEDULER) uiScheduler: Scheduler) : BreedDetailsViewModel {
        return BreedDetailsViewModel(breedsRepository, ioScheduler, uiScheduler)
    }
}