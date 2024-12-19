package moe.emi.wanikani.type.body

import kotlinx.serialization.Serializable
import moe.emi.wanikani.type.ID
import moe.emi.wanikani.type.Timestamp

@Serializable
data class CreateReviewRequest(
	val review: Review
) {
	@Serializable
	data class Review(
		val assignmentId: ID? = null,
		val subjectId: ID? = null,
		val incorrectMeaningAnswers: Int,
		val incorrectReadingAnswers: Int,
		val createdAt: Timestamp? = null,
	)
}


