package moe.emi.wanikani.response

import io.ktor.client.call.body
import io.ktor.http.HttpHeaders
import io.ktor.http.etag
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import moe.emi.wanikani.request.Request
import moe.emi.wanikani.type.httpDateToTimestamp

sealed interface Response<out A> {
	
	data class Success<A>(
		val body: A,
		val etag: String? = null,
		val lastModified: Instant? = null
	) : Response<A>
	
	// TODO better failure
	data class Failure(
		val code: Int,
		val message: String? = null,
	) : Response<Nothing>
	
	fun getOrThrow() = (this as Success).body
}

@Serializable
data class ErrorBody(
	val error: String?,
	val code: Int
)

suspend fun <A> Request<A>.execute(): Response<A> =
	fold({ response, body ->
		Response.Success(
			body,
			response.etag(),
			response.headers[HttpHeaders.LastModified]?.let(::httpDateToTimestamp)
		)
	}, { response ->
		Response.Failure(
			response.status.value,
			runCatching<ErrorBody> { response.body() }.getOrNull()?.error
		)
	})