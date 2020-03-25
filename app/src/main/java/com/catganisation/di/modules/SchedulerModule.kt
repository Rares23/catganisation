package com.catganisation.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class SchedulerModule {
    companion object {
        const val IO_SCHEDULER: String = "io"
        const val UI_SCHEDULER: String = "ui"
    }

    @Provides
    @Singleton
    @Named(IO_SCHEDULER)
    fun provideIOScheduler() : Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @Named(UI_SCHEDULER)
    fun provideUIScheduler() : Scheduler {
        return AndroidSchedulers.mainThread()
    }
}