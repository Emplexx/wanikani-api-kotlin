package moe.emi.wanikani.type

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
	val lessons: List<Lesson>,
	val reviews: List<Review>,
	val nextReviewsAt: Instant?
) {
	/**
	 * @property availableAt When the paired [subjectIds] are available for lessons. Always beginning of the current hour when the API endpoint is accessed.
	 * @property subjectIds Collection of unique identifiers for subjects.
	 */
	@Serializable
	data class Lesson(val availableAt: Instant, val subjectIds: List<Int>)
	
	/**
	 * @property availableAt When the paired [subjectIds] are available for reviews. All timestamps are the top of an hour.
	 * @property subjectIds Collection of unique identifiers for subjects.
	 */
	@Serializable
	data class Review(val availableAt: Instant, val subjectIds: List<Int>)
}


