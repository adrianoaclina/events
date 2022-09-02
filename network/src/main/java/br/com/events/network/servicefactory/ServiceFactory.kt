package br.com.events.network.servicefactory

interface ServiceFactory {
    fun <T> create(service: Class<T>): T
}