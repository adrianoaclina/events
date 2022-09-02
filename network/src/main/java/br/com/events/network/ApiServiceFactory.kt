package br.com.events.network

import br.com.events.network.httpclient.OkHttpClientFactory
import br.com.events.network.servicefactory.ServiceFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServiceFactory (
    okHttpClientFactory: OkHttpClientFactory
) : ServiceFactory {

    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClientFactory.create())
        .build()

    override fun <T> create(service: Class<T>): T = retrofit.create(service)

}