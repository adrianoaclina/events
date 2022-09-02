package br.com.events.home.presentation.eventdetails

import br.com.events.home.domain.entity.Event
import br.com.events.network.entity.Async

data class EventDetailsState(
    val event: Async<Event>
)