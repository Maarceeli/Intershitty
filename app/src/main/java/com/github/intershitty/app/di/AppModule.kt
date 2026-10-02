package com.github.intershitty.app.di

import androidx.room3.Room
import com.github.intershitty.app.database.AppDatabase
import com.github.intershitty.app.ui.screens.dashboard.DashboardViewModel
import com.github.intershitty.app.ui.screens.login.LogInViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "intershitty.db"
        ).build()
    }

    single { get<AppDatabase>().accountDao() }
    single { get<AppDatabase>().ticketDao() }

    viewModelOf(::LogInViewModel)
    viewModelOf(::DashboardViewModel)
}
