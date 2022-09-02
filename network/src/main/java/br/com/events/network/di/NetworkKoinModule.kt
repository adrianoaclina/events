package br.com.events.network.di

import br.com.events.network.ApiOkHttpClientFactory
import br.com.events.network.ApiServiceFactory
import br.com.events.network.executor.SafeRequest
import br.com.events.network.httpclient.OkHttpClientFactory
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectNetworkKoinModule() = loadKoinModule

private val loadKoinModule by lazy {
    loadKoinModules(
        module {
            factory<OkHttpClientFactory> {
                ApiOkHttpClientFactory()
            }
            factory { SafeRequest(Dispatchers.IO) }
            single { ApiServiceFactory(get()) }
        }
    )
}