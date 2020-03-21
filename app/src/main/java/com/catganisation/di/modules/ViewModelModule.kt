package com.catganisation.di.modules

import com.catganisation.data.repositories.BreedRepository
import com.catganisation.ui.viewmodels.BreedsListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideBreedsListViewModel(breedsRepository: BreedRepository) : BreedsListViewModel {
        return BreedsListViewModel(breedsRepository)
    }
}