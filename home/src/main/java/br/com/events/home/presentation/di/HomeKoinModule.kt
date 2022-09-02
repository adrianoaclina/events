package br.com.events.home.presentation.di

import br.com.events.home.data.repository.CheckInRepositoryImpl
import br.com.events.home.data.repository.EventResultRepositoryImpl
import br.com.events.home.data.service.CheckInService
import br.com.events.home.data.service.EventResultService
import br.com.events.home.domain.repository.CheckInRepository
import br.com.events.home.domain.repository.EventResultRepository
import br.com.events.home.presentation.checkin.CheckInViewModel
import br.com.events.home.presentation.eventdetails.EventDetailsViewModel
import br.com.events.home.presentation.homefragment.HomeViewModel
import br.com.events.network.ApiServiceFactory
import br.com.events.network.executor.SafeRequest
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectHomeKoinModule() = loadKoinModule

private val loadKoinModule by lazy {
    loadKoinModules(
        listOf(
            serviceModule,
            repositoryModule,
            viewModelModule
        )
    )
}

private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { EventDetailsViewModel(get()) }
    viewModel { CheckInViewModel(get()) }
}
private val repositoryModule = module {
    single<EventResultRepository> { EventResultRepositoryImpl(get(), get()) }
    single<CheckInRepository> { CheckInRepositoryImpl(get()) }
    factory { SafeRequest(Dispatchers.IO) }
}
private val serviceModule = module {
    single { get<ApiServiceFactory>().create(EventResultService::class.java) }
    single { get<ApiServiceFactory>().create(CheckInService::class.java) }
}