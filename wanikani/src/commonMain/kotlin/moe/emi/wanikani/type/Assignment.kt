package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

/**
 * Assignments contain information about a user's progress on a particular [Subject], including their current state and timestamps for various progress milestones. Assignments are created when a user has passed all the components of the given [Subject] and the assignment is at or below their current level for the first time.
 *
 * **Note:** The [unlockedAt], [startedAt], [passedAt], and [burnedAt] timestamps are always in sequential order — assignments can't be started before they're unlocked, passed before they're started, etc.
 */
@Serializable
data class Assignment(
	val createdAt: Timestamp?,
	val subjectId: ID,
	val subjectType: SubjectType,
	val srsStage: Int,
	val unlockedAt: Timestamp?,
	val startedAt: Timestamp?,
	val passedAt: Timestamp?,
	val burnedAt: Timestamp?,
	val availableAt: Timestamp?,
	val resurrectedAt: Timestamp?,
	val hidden: Boolean,
	val level: Int? = null
)