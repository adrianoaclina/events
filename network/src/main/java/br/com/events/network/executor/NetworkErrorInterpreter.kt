package br.com.events.network.executor

import br.com.events.network.entity.ErrorType
import br.com.events.network.entity.Result
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object NetworkErrorInterpreter {
    private const val ERROR = "error"
    private const val MESSAGE = "message"
    private const val CODE = "code"

    fun interpret(throwable: Throwable) = getErrorType(throwable)

    private fun getErrorType(throwable: Throwable) = with(throwable) {
        when (this) {
            is IOException -> if (this is SocketTimeoutException) {
                getDefaultErrorWithType(ErrorType.Timeout)
            } else {
                getDefaultErrorWithType(ErrorType.NoConnection)
            }
            is HttpException -> {
                val errorBody = runCatching { response()?.errorBody()?.string() }.getOrNull()
                Result.Error(
                    message = interpretErrorMessage(errorBody),
                    code = interpretCode(errorBody),
                    throwable = this,
                    type = getErrorTypeFromHttpCode()
                )
            }
            else -> getDefaultErrorWithType(ErrorType.Unknown)
        }
    }

    private fun Throwable.getDefaultErrorWithType(type: ErrorType) = Result.Error(
        message = message.toString(),
        throwable = this,
        type = type
    )

    private fun interpretErrorMessage(errorBody: String?): String {
        val error = getErrorFromResponseBody(errorBody)
        val errorMessage = getMessageFromResponseBody(errorBody)
        return error.takeIf { !it.isNullOrBlank() } ?: errorMessage ?: ""
    }

    private fun getErrorFromResponseBody(errorBody: String?) =
        parseParameterFromBody(errorBody, ERROR)

    private fun getMessageFromResponseBody(errorBody: String?) =
        parseParameterFromBody(errorBody, MESSAGE)

    private fun interpretCode(errorBody: String?) =
        parseParameterFromBody(errorBody, CODE)

    private fun parseParameterFromBody(errorBody: String?, parameter: String) = runCatching {
        errorBody?.let { JSONObject(it).getString(parameter) }
    }.getOrNull()

    private fun HttpException.getErrorTypeFromHttpCode() = when {
        code() == 400 -> ErrorType.BadRequest
        code() == 401 -> ErrorType.Unauthorized
        code() == 403 -> ErrorType.AccessDenied
        code() == 404 -> ErrorType.NotFound
        code() > 500 -> ErrorType.ServerDown
        else -> ErrorType.Unknown
    }
}