package br.com.events.home.data.service

import br.com.events.home.data.entity.CheckInResultRemote
import br.com.events.home.domain.entity.CheckIn
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckInService {

    @POST("checkin")
    fun sendCheckIn(
        @Body checkIn: CheckIn,
    ): Call<CheckInResultRemote>
}