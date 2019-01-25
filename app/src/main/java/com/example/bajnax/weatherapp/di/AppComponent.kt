package com.example.bajnax.weatherapp.di

import com.example.bajnax.weatherapp.presentation.presenter.blank.ChartPresenter
import com.example.bajnax.weatherapp.presentation.presenter.blank.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class), (ContextModule::class), (RoomModule::class)])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)
    fun inject(chartPresenter: ChartPresenter)
}