package com.rysanek.rickandmorty.application

import android.app.Application
import com.rysanek.network_utils.coroutines.initializeConnectivityObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApplication: Application (){
    override fun onCreate() {
        super.onCreate()
        initializeConnectivityObserver()
    }
}