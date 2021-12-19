package com.diegopizzo.asostechtest.config

import android.app.Application
import com.diegopizzo.asostechtest.BuildConfig
import com.diegopizzo.asostechtest.ui.config.viewModelModule
import com.diegopizzo.network.cache.cacheInteractorModule
import com.diegopizzo.network.creator.creatorModule
import com.diegopizzo.network.interactor.interactorModule
import com.diegopizzo.network.service.retrofitModule
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AsosTechTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { }
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@AsosTechTestApplication)
            modules(
                retrofitModule(BuildConfig.BASE_URL),
                cacheInteractorModule,
                creatorModule,
                interactorModule,
                viewModelModule
            )
        }
    }
}