package moe.emi.wanikani.type.body

import kotlinx.serialization.Serializable
import moe.emi.wanikani.type.PresentationOrder

@Serializable
data class UpdateUserRequest(
	val user: User
) {
	
	@Serializable
	data class User(
		val extraStudyAutoplayAudio: Boolean?,
		val lessonsAutoplayAudio: Boolean?,
		val lessonsBatchSize: Int?,
		val reviewsAutoplayAudio: Boolean?,
		val reviewsDisplaySrsIndicator: Boolean?,
		val reviewsPresentationOrder: PresentationOrder?,
	)
}
