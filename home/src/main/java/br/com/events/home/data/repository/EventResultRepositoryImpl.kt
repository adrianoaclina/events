package br.com.events.home.data.repository

import br.com.events.home.data.service.EventResultService
import br.com.events.home.domain.repository.EventResultRepository
import br.com.events.network.executor.SafeRequest

class EventResultRepositoryImpl(
    private val eventResultService: EventResultService,
    private val safeRequest: SafeRequest
): EventResultRepository {

    override suspend fun getEvents() = safeRequest{
        eventResultService.getEvents()
    }

    override suspend fun getEvent(eventId: String) = safeRequest {
        eventResultService.getEvent(eventId).transform()
    }

}