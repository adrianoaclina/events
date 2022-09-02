package br.com.events.home.data.repository

import br.com.events.home.data.entity.CheckInResultRemote
import br.com.events.home.data.service.CheckInService
import br.com.events.home.domain.entity.CheckIn
import br.com.events.home.domain.repository.CheckInRepository
import br.com.events.network.call.executeRequest

class CheckInRepositoryImpl(
    private val checkInService: CheckInService
) : CheckInRepository {

    override suspend fun sendCheckIn(
        eventId: String,
        name: String,
        email: String,
        isSuccess: (success: CheckInResultRemote?) -> Unit,
        isError: (error: String?) -> Unit
    ) {
        executeRequest(
            checkInService.sendCheckIn(
                CheckIn(eventId, name, email)
            ),
            isSuccess = isSuccess,
            isError = isError
        )
    }
}