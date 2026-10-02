package com.github.intershitty.app

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.github.intershitty.api.apiInterceptors
import com.github.intershitty.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntershittyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        apiInterceptors.add(ChuckerInterceptor(this))
        
        startKoin {
            androidContext(this@IntershittyApp)
            modules(appModule)
        }
    }
}
