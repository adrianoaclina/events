package br.com.events.network.call

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> executeRequest(
    call: Call<T>,
    isSuccess: (success: T?) -> Unit,
    isError: (error: String?) -> Unit
) {
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                isSuccess(response.body())
            } else {
                isError("Error: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            isError(t.message)
        }
    })
}