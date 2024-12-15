package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

// TODO format docs
/**
 * Level progressions contain information about a user's progress through the WaniKani levels.
 *
 * A level progression is created when a user has met the prerequisites for leveling up, which are:
 * - Reach a 90% passing rate on assignments for a user's current level with a [SubjectType] of [Kanji]. Passed assignments have data.passed equal to true and a data.passed_at that's in the past.
 * - Have access to the level. Under /user, the data.level must be less than or equal to data.subscription.max_level_granted.
 *
 *
 * **Note:** The [unlockedAt], [startedAt], [passedAt], and [completedAt] timestamps are always in sequential order — level progressions can't be started before they're unlocked, passed before they're started, etc.
 */
@Serializable
data class LevelProgression(
	val level: Int,
	val unlockedAt: Timestamp?,
	val startedAt: Timestamp?,
	val passedAt: Timestamp?,
	val completedAt: Timestamp?,
	val abandonedAt: Timestamp?,
	val createdAt: Timestamp?,
)