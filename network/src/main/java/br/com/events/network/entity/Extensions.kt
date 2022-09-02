package br.com.events.network.entity

fun <T, U> Result<T>.combine(second: Result<U>) = when (this) {
    is Result.Success -> when (second) {
        is Result.Success -> Result.Success(Pair(this.data, second.data))
        is Result.Error -> second
    }
    is Result.Error -> this
}

inline fun <T> Result<T>.onError(lambda: (Result.Error) -> Unit): Result<T> {
    if (this is Result.Error) lambda.invoke(this)
    return this
}

inline fun <T> Result<T>.onErrorMessage(lambda: (String) -> Unit): Result<T> {
    if (this is Result.Error) lambda.invoke(this.message)
    return this
}

inline fun <T> Result<T>.onSuccess(lambda: (T) -> Unit): Result<T> {
    if (this is Result.Success) lambda.invoke(data)
    return this
}

inline fun <T, R> Result<T>.mapSuccess(map: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> this
    }
}

fun <T> Result<T>.isSuccess() = this is Result.Success

fun <T> Result<T>.isError() = this is Result.Error

fun <T> Result<T>.getOrThrow(): T {
    when (this) {
        is Result.Error -> throw throwable
        is Result.Success -> return data
    }
}

fun <T> Result<T>.getOrDefault(defaultValue: T): T {
    return when (this) {
        is Result.Error -> defaultValue
        is Result.Success -> this.data
    }
}

fun <T> Result<T>.getOrNull(): T? {
    return getOrDefault(null)
}