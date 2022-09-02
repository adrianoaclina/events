package br.com.events.home.presentation.checkin

import br.com.events.network.entity.Async

data class CheckInState(
    val checkIn: Async<String>? = null
)