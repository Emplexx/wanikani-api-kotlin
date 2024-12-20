package moe.emi.wanikani.type

import kotlinx.serialization.Serializable
import moe.emi.wanikani.Wanikani

/**
 * Level progressions contain information about a user's progress through the WaniKani levels.
 *
 * A level progression is created when a user has met the prerequisites for leveling up, which are:
 * - Reach a 90% passing rate on assignments for a user's current level with a [SubjectType] of [Kanji].
 * Passed assignments have [Assignment.passed] equal to true and a [Assignment.passedAt] that's in the past.
 * - Have access to the level. Under [Wanikani.getUser], [User.level] must be less than or equal to [User.subscription].[Subscription.maxLevelGranted].
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