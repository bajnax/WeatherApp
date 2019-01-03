package com.example.bajnax.weatherapp.di

import com.example.bajnax.weatherapp.presentation.presenter.blank.MainPresenter
import dagger.Component


@Component(modules = [(NetworkModule::class), (ContextModule::class)])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)
}