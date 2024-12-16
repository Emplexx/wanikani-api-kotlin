package moe.emi.wanikani

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import moe.emi.wanikani.serialization.WanikaniJson

internal fun HttpClient.withWanikaniConfig(auth: AuthProvider, revision: Int) = config {
	
	defaultRequest {
		url("https://api.wanikani.com/v2/")
		headers {
			append("Wanikani-Revision", revision.toString())
		}
	}
	
	install(authPlugin(auth))
	
	install(ContentNegotiation) {
		json(WanikaniJson)
	}
	
}

private fun authPlugin(provider: AuthProvider) = createClientPlugin("auth") {
	onRequest { request, _ ->
		provider.getAuthToken()?.let { request.bearerAuth(it) }
	}
}
