package org.thanhtd.currencyapp.di

import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.thanhtd.currencyapp.data.local.MongoImpl
import org.thanhtd.currencyapp.data.local.PreferencesImpl
import org.thanhtd.currencyapp.data.remote.api.CurrencyApiServiceImpl
import org.thanhtd.currencyapp.domain.CurrencyApiService
import org.thanhtd.currencyapp.domain.MongoRepository
import org.thanhtd.currencyapp.domain.PreferencesRepository
import org.thanhtd.currencyapp.presentation.screen.HomeViewModel

val appModule = module {
    single { Settings() }
    single<MongoRepository> { MongoImpl() }
    single<PreferencesRepository> { PreferencesImpl(settings = get()) }
    single<CurrencyApiService> { CurrencyApiServiceImpl(preferences = get()) }
    factory {
        HomeViewModel(
            preferences = get(),
            mongoDb = get(),
            currencyApiService = get()
        )
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}