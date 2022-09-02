package br.com.events.home.presentation.homefragment

interface HomeDelegate {
    fun navToEventDetails(eventId: String, eventName: String, eventPrice: String)
}