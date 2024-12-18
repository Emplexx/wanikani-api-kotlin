package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

/**
 * Review statistics summarize the activity recorded in [Review]s. They contain sum the number of correct and incorrect answers for both meaning and reading. They track current and maximum streaks of correct answers. They store the overall percentage of correct answers versus total answers.
 *
 * A review statistic is created when the user has done their first review on the related subject.
 *
 * @property subjectId ID of the associated [Subject].
 * @property subjectType Type of the associated [Subject].
 * @property meaningCorrect Total number of correct answers submitted for the meaning of the associated subject.
 * @property meaningIncorrect Total number of incorrect answers submitted for the meaning of the associated subject.
 * @property meaningCurrentStreak The current, uninterrupted series of correct answers given for the meaning of the associated subject.
 * @property meaningMaxStreak The longest, uninterrupted series of correct answers ever given for the meaning of the associated subject.
 * @property readingCorrect Total number of correct answers submitted for the reading of the associated subject.
 * @property readingIncorrect Total number of incorrect answers submitted for the reading of the associated subject.
 * @property readingCurrentStreak The current, uninterrupted series of correct answers given for the reading of the associated subject.
 * @property readingMaxStreak The longest, uninterrupted series of correct answers ever given for the reading of the associated subject.
 * @property percentageCorrect The overall correct answer rate by the user for the subject, including both meaning and reading.
 * @property createdAt Timestamp when the review statistic was created.
 * @property hidden Indicates if the associated subject has been hidden, preventing it from appearing in lessons or reviews.
 */
@Serializable
data class ReviewStatistic(
	val subjectId: Int,
	val subjectType: SubjectType,
	
	val meaningCorrect: Int,
	val meaningIncorrect: Int,
	val meaningCurrentStreak: Int,
	val meaningMaxStreak: Int,
	
	val readingCorrect: Int,
	val readingIncorrect: Int,
	val readingCurrentStreak: Int,
	val readingMaxStreak: Int,
	
	val percentageCorrect: Int,
	
	val createdAt: Timestamp,
	val hidden: Boolean,
) {
	/**
	 * Calculate the percentage of correct answers for the subject using present data.
	 */
	fun calculatePercentageCorrect(): Double {
		val totalCorrect = meaningCorrect + readingCorrect
		val total = meaningCorrect + readingCorrect + meaningIncorrect + readingIncorrect
		return (totalCorrect.toDouble() / total) * 100.0
	}
}