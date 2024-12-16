@file:Suppress("unused")

package moe.emi.wanikani.request

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.ifModifiedSince
import io.ktor.http.ifNoneMatch
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import moe.emi.wanikani.type.ResourceSet
import moe.emi.wanikani.type.SubjectType
import moe.emi.wanikani.type.Timestamp
import moe.emi.wanikani.type.toJavaDate

sealed class Request<out A>(
	internal val client: HttpClient,
	internal val typeInfo: TypeInfo,
	internal val builder: HttpRequestBuilder
) {
	
	internal abstract suspend fun request(): HttpResponse
	
	abstract suspend fun <B> fold(
		success: suspend (HttpResponse, A) -> B,
		failure: suspend (HttpResponse) -> B
	): B
	
	fun builder(block: HttpRequestBuilder.() -> Unit) = this.apply { builder.apply(block) }
}

class RegularRequest<A>(
	client: HttpClient,
	typeInfo: TypeInfo,
	builder: HttpRequestBuilder
) : Request<A>(client, typeInfo, builder) {
	override suspend fun request(): HttpResponse = client.request(builder)
	
	override suspend fun <B> fold(
		success: suspend (HttpResponse, A) -> B,
		failure: suspend (HttpResponse) -> B
	): B {
		val res = request()
		return when {
			res.status.value in 200..299 -> success(res, res.body(typeInfo))
			else -> failure(res)
		}
	}
}

class ConditionalRequest<A>(
	client: HttpClient,
	typeInfo: TypeInfo,
	builder: HttpRequestBuilder
) : Request<A?>(client, typeInfo, builder) {
	var ifModifiedSince: Timestamp? = null
	var ifNoneMatch: String? = null
	
	override suspend fun request(): HttpResponse =
		client.request(builder.apply {
			ifModifiedSince?.let { ifModifiedSince(it.toJavaDate()) }
			ifNoneMatch?.let { ifNoneMatch(it) }
		})
	
	override suspend fun <B> fold(
		success: suspend (HttpResponse, A?) -> B,
		failure: suspend (HttpResponse) -> B
	): B {
		val res = request()
		return when {
			res.status.value in 200..299 -> success(res, res.body(typeInfo))
			res.status == HttpStatusCode.NotModified -> success(res, null)
			else -> failure(res)
		}
	}
	
}

@Suppress("UNCHECKED_CAST")
fun <A> Request<A>.ifModifiedSince(instant: Timestamp): Request<A?> = when (this) {
	is RegularRequest -> ConditionalRequest(client, typeInfo, builder)
	else -> this as ConditionalRequest<A>
}.apply { ifModifiedSince = instant }

@Suppress("UNCHECKED_CAST")
fun <A> Request<A>.ifNoneMatch(etag: String): Request<A?> = when (this) {
	is RegularRequest -> ConditionalRequest(client, typeInfo, builder)
	else -> this as ConditionalRequest<A>
}.apply { ifNoneMatch = etag }

fun <A> Request<ResourceSet<A>>.updatedAfter(instant: Timestamp): Request<ResourceSet<A>> =
	builder {
		parameter("updated_after", instant.toString())
	}

inline fun <A> HttpClient.createRequest(
	method: HttpMethod,
	typeInfo: TypeInfo,
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): Request<A> =
	RegularRequest(this, typeInfo, HttpRequestBuilder().apply {
		this.method = method
		url(urlString)
		block()
	})

inline fun <A> HttpClient.getRequest(
	typeInfo: TypeInfo,
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): Request<A> = createRequest(HttpMethod.Get, typeInfo, urlString, block)

inline fun <reified A> HttpClient.getRequest(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): Request<A> = getRequest(typeInfo<A>(), urlString, block)

inline fun <reified A> HttpClient.postRequest(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): Request<A> = createRequest(HttpMethod.Post, typeInfo<A>(), urlString, block)

inline fun <reified A> HttpClient.putRequest(
	urlString: String,
	block: HttpRequestBuilder.() -> Unit = {}
): Request<A> = createRequest(HttpMethod.Put, typeInfo<A>(), urlString, block)

fun HttpRequestBuilder.parameters(vararg parameters: Pair<String, Any?>) {
	
	parameters.forEach { (k, v) ->
		when (v) {
			is Iterable<*> -> v
				.map {
					if (it is SubjectType) it.value else it
				}
				.joinToString(",")
				.ifEmpty { return@forEach }
				.let { parameter(k, it) }
			
			// TODO test if Present (Unit) work correctly
			
			null -> return@forEach
			else -> parameter(k, v)
		}
	}
}
