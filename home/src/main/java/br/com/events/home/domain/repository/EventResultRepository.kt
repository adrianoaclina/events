package br.com.events.home.domain.repository

import br.com.events.home.domain.entity.Event
import br.com.events.network.entity.Result

interface EventResultRepository {

    suspend fun getEvents(): Result<List<Event>>

    suspend fun getEvent(eventId: String) : Result<Event>
}