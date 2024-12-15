package moe.emi.wanikani.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@OptIn(ExperimentalSerializationApi::class)
val WanikaniJson = Json {
	namingStrategy = JsonNamingStrategy.SnakeCase
	prettyPrint = true
	
	ignoreUnknownKeys = true
}