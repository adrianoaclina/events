package br.com.events.network.httpclient

import okhttp3.OkHttpClient

interface OkHttpClientFactory {
    fun create(): OkHttpClient
}