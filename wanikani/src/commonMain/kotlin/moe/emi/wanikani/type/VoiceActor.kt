package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

@Serializable
data class VoiceActor(
	val description: String,
	val gender: String,
	val name: String,
)
