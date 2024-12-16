package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

// TODO add docs https://docs.api.wanikani.com/20170710/#review-statistic-data-structure
@Serializable
data class ReviewStatistic(
	val createdAt: Timestamp,
	val hidden: Boolean,
	val meaningCorrect: Int,
	val meaningCurrentStreak: Int,
	val meaningIncorrect: Int,
	val meaningMaxStreak: Int,
	val percentageCorrect: Int,
	val readingCorrect: Int,
	val readingCurrentStreak: Int,
	val readingIncorrect: Int,
	val readingMaxStreak: Int,
	val subjectId: Int,
	val subjectType: SubjectType
) {
	fun calculatePercentageCorrect(): Double {
		val totalCorrect = meaningCorrect + readingCorrect
		val total = meaningCorrect + readingCorrect + meaningIncorrect + readingIncorrect
		return (totalCorrect.toDouble() / total) * 100.0
	}
}