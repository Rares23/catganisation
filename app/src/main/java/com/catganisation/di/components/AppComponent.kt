package com.catganisation.di.components

import com.catganisation.di.modules.AppModule
import com.catganisation.ui.activities.BreedDetailsActivity
import com.catganisation.ui.activities.BreedsListActivity
import com.catganisation.ui.activities.FiltersActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(breedsListActivity: BreedsListActivity)
    fun inject(breedDetailsActivity: BreedDetailsActivity)
    fun inject(filtersActivity: FiltersActivity)
}