package moe.emi.wanikani.type

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * The summary report contains currently available lessons and reviews and the reviews that will become available in the next 24 hours, grouped by the hour.
 * @property nextReviewsAt Earliest date when the reviews are available. Is `null` when the user has no reviews scheduled.
 */
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


