package br.com.events.home.data.service

import br.com.events.home.domain.entity.Event
import br.com.events.home.domain.entity.HomeResultItemType
import retrofit2.http.GET
import retrofit2.http.Path

interface EventResultService {

    @GET("events")
    suspend fun getEvents(): List<Event>

    @GET("events/{id}")
    suspend fun getEvent(
        @Path("id") eventId: String
    ) : HomeResultItemType
}