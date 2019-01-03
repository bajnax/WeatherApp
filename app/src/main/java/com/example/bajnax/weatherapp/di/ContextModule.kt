package com.example.bajnax.weatherapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object ContextModule {

    @Provides
    @JvmStatic
    internal fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }

}