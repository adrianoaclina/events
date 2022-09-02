package br.com.events.network.entity

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val throwable: Throwable = EmptyThrowable,
        val code: String? = null,
        val type: ErrorType = ErrorType.Unknown
    ) : Result<Nothing>()
}
