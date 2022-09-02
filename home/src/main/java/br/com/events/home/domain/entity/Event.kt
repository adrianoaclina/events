package br.com.events.home.domain.entity

data class Event(
    val people: List<People>,
    val date: Long,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: String
)
