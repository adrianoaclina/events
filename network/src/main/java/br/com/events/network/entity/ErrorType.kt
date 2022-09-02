package br.com.events.network.entity

sealed class ErrorType {
    object Unauthorized : ErrorType()
    object BadRequest : ErrorType()
    object NoConnection : ErrorType()
    object Timeout : ErrorType()
    object NotFound : ErrorType()
    object AccessDenied : ErrorType()
    object ServerDown : ErrorType()
    object Unknown : ErrorType()
}
