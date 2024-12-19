package moe.emi.wanikani.type

import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Assignments contain information about a user's progress on a particular [Subject], including their current state and timestamps for various progress milestones. Assignments are created when a user has passed all the components of the given [Subject] and the assignment is at or below their current level for the first time.
 *
 * **Note:** The [unlockedAt], [startedAt], [passedAt], and [burnedAt] timestamps are always in sequential order — assignments can't be started before they're unlocked, passed before they're started, etc.
 *
 * @property subjectId Unique identifier of the associated [Subject].
 * @property subjectType The [SubjectType] of the associated [Subject].
 * @property srsStage The current [Srs] stage interval. The interval range is determined by the related subject's spaced repetition system.
 * @property createdAt Timestamp when the assignment was created.
 * @property availableAt Timestamp when the related subject will be available in the user's review queue.
 * @property resurrectedAt Timestamp when the subject is resurrected and placed back in the user's review queue.
 *
 * @property unlockedAt The timestamp when the related subject has its prerequisites satisfied and is made available in lessons. Prerequisites are:
 * - The subject components have reached SRS stage `5` once (they have been “passed”).
 * - The user's level is equal to or greater than the level of the assignment’s subject.
 * @property startedAt Timestamp when the user completes the lesson for the related subject.
 * @property passedAt Timestamp when the user reaches SRS stage `5` for the first time.
 * @property burnedAt Timestamp when the user reaches SRS stage `9` the first time.
 *
 * @property hidden Indicates if the associated subject has been hidden, preventing it from appearing in lessons or reviews.
 */
@Serializable
data class Assignment(
	val subjectId: ID,
	val subjectType: SubjectType,
	val srsStage: Int,
	val createdAt: Timestamp?,
	val availableAt: Timestamp?,
	val resurrectedAt: Timestamp?,
	
	val unlockedAt: Timestamp?,
	val startedAt: Timestamp?,
	val passedAt: Timestamp?,
	val burnedAt: Timestamp?,
	
	val hidden: Boolean,
	val passed: Boolean,
	val level: Int? = null
)

/**
 * Calculate the SRS stage of an assignment after [incorrectAnswersCount] incorrect answers.
 * [Source](https://knowledge.wanikani.com/wanikani/srs-stages/#how-does-it-work)
 */
fun calculateNewSrsStage(
	currentSrsStage: Int,
	incorrectAnswersCount: Int,
): Int = when (incorrectAnswersCount) {
	0 -> currentSrsStage + 1
	else -> {
		val incorrectAdjustmentCount = incorrectAnswersCount.toDouble().div(2).roundToInt()
		val srsPenaltyFactor = if (currentSrsStage >= 5) 2 else 1
		max(1, currentSrsStage - (incorrectAdjustmentCount * srsPenaltyFactor))
	}
}