package br.com.events.home.domain.repository

import br.com.events.home.data.entity.CheckInResultRemote

interface CheckInRepository {

    suspend fun sendCheckIn(
        eventId: String,
        name: String,
        email: String,
        isSuccess: (success: CheckInResultRemote?) -> Unit,
        isError: (error: String?) -> Unit
    )
}