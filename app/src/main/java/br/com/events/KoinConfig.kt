package br.com.events

import br.com.events.home.presentation.di.injectHomeKoinModule
import br.com.events.network.di.injectNetworkKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun startKoin(myApplication: MyApplication){
    startKoin {
        androidContext(myApplication)
    }
    injectModules()
}

fun injectModules() {
    injectNetworkKoinModule()
    injectHomeKoinModule()
}
