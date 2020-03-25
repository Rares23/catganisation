package com.catganisation.app

import android.app.Application
import com.catganisation.di.components.AppComponent
import com.catganisation.di.components.DaggerAppComponent
import com.catganisation.di.modules.AppModule

class CatganisationApplication : Application()  {

    companion object {
        lateinit var app: Application
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        app = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()

    }

    fun getAppComponent() : AppComponent {
        return appComponent
    }
}