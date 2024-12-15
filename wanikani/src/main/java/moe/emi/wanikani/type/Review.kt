package moe.emi.wanikani.type

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * # Notes
 * ###### Incorrect Answers
 *
 * A [Subject] ([Radical], [Kanji], [Vocabulary], and [KanaVocabulary]) may not require a meaning or reading. Therefore properties [incorrectMeaningAnswers] and [incorrectReadingAnswers] will return a value of 0 for subjects which do not have the requirement.
 *
 * | **Subject type** 	| **Answer types allowed** |
 * | :----------------- | :----------------------- |
 * | [KanaVocabulary]	| Meaning |
 * | [Kanji]        	| Meaning, Reading |
 * | [Radical] 			| Meaning |
 * | [Vocabulary] 		| Meaning, Reading |
 *
 * ###### Spaced Repetition System
 * The associated spaced repetition system is the system used to do the SRS stage calculations at the time the review record was created. It does not necessarily mean it is the current [Srs] associated to [Subject]. This is done to preserve history.
 *
 * @property assignmentId ID of the associated [Assignment].
 * @property subjectId ID of the associated [Subject].
 * @property spacedRepetitionSystemId ID of the associated [Srs].
 * @property createdAt Timestamp when the review was created.
 * @property startingSrsStage The starting SRS stage interval, with valid values ranging from 1 to 8.
 * @property endingSrsStage The SRS stage interval calculated from the number of correct and incorrect answers, with valid values ranging from 1 to 9
 * @property incorrectMeaningAnswers The number of times the user has answered the meaning incorrectly.
 * @property incorrectReadingAnswers The number of times the user has answered the reading incorrectly.
 */
@Serializable
data class Review(
	val assignmentId: ID,
	val subjectId: ID,
	val spacedRepetitionSystemId: ID,
	
	val createdAt: Instant,
	val startingSrsStage: Int,
	val endingSrsStage: Int,
	
	val incorrectMeaningAnswers: Int,
	val incorrectReadingAnswers: Int,
)

@Serializable
data class CreateReviewResponse(
	val id: Int,
	
	override val `object`: ObjectType,
	override val url: String,
	override val dataUpdatedAt: Instant,
	override val data: Review,
	
	val resourcesUpdated: ResourcesUpdated
) : WanikaniResponseBody<Review> {
	@Serializable
	data class ResourcesUpdated(
		val assignment: Assignment,
		val reviewStatistic: ReviewStatistic,
	)
}

sealed interface CreateReviewFor {
	val assignmentId: ID? get() = null
	val subjectId: ID? get() = null
	
	data class Assignment(override val assignmentId: ID) : CreateReviewFor
	data class Subject(override val subjectId: ID) : CreateReviewFor
}