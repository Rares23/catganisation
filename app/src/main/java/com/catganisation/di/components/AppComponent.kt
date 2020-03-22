package com.catganisation.di.components

import com.catganisation.di.modules.AppModule
import com.catganisation.ui.activities.BreedsListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(breedsListActivity: BreedsListActivity)
}