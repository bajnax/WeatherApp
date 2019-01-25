package com.example.bajnax.weatherapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ContextModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Context {
        return application
    }

}