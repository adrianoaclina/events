package br.com.events.home.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class HomeResultItemType(
    @field:Json(name = "people") private val people: List<People>,
    @field:Json(name = "date") private val date: Long,
    @field:Json(name = "description") private val description: String,
    @field:Json(name = "image") private val image: String,
    @field:Json(name = "longitude") private val longitude: Double,
    @field:Json(name = "latitude") private val latitude: Double,
    @field:Json(name = "price") private val price: Double,
    @field:Json(name = "title") private val title: String,
    @field:Json(name = "id") private val id: String
) {
    fun transform() = Event(
        people, date, description, image, longitude, latitude, price, title, id
    )
}
