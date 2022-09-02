package br.com.events.home.presentation.checkin

sealed class CheckInInteractions {
    data class CheckInClicked(
        val eventId: String,
        val name: String,
        val email: String
    ) : CheckInInteractions()
}