package com.example.eznotes

import android.app.Application
import com.example.eznotes.di.AppContainer
import com.example.eznotes.di.DefaultAppContainer

class EzNotesApplication : Application() {
    
    lateinit var container: AppContainer
        private set
        
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}