package br.com.events.home.presentation.eventdetails

sealed class EventDetailsInteractions {
    data class Opened(
        val eventId: String
    ) : EventDetailsInteractions()
}