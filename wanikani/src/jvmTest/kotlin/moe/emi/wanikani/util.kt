package moe.emi.wanikani

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.encodeToString
import moe.emi.wanikani.serialization.WanikaniJson

fun getToken(): String = System.getenv("token")

fun getTestClient(token: String = getToken()): Wanikani = Wanikani {
	authProvider = AuthProvider { token }
	httpClient = HttpClient {
		install(Logging)
		install(HttpTimeout) {
			requestTimeoutMillis = null
			connectTimeoutMillis = null
			socketTimeoutMillis = null
		}
	}
}

inline fun <reified T> T.prettyPrintJson() = also {
	WanikaniJson.encodeToString(it).let(::println)
}