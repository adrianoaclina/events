package br.com.events.home.presentation.homefragment

import br.com.events.home.domain.entity.Event
import br.com.events.network.entity.Async

data class HomeState(
    val events: Async<List<Event>>
)