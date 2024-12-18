package moe.emi.wanikani.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.times

/**
 * Available spaced repetition systems used for calculating stage changes to Assignments ([Assignment.srsStage]) and Reviews ([Review.startingSrsStage], [Review.endingSrsStage]). Has relationship with [Subject]s.
 *
 * The `*Position` fields align with the timestamps on [Assignment]:
 * - [unlockingStagePosition] -> [Assignment.unlockedAt],
 * - [startingStagePosition] -> [Assignment.startedAt],
 * - etc.
 *
 * @property name The name of the spaced repetition system
 * @property description Details about the spaced repetition system.
 * @property unlockingStagePosition `position` of the unlocking stage.
 * @property startingStagePosition `position` of the starting stage.
 * @property passingStagePosition `position` of the passing stage.
 * @property burningStagePosition `position` of the burning stage.
 * @property createdAt Timestamp when the SRS was created.
 */
@Serializable
data class Srs(
	val name: String,
	val description: String,
	val unlockingStagePosition: Int,
	val startingStagePosition: Int,
	val passingStagePosition: Int,
	val burningStagePosition: Int,
	val stages: List<Stage>,
	val createdAt: Timestamp,
)

/**
 * **Note:** The unlocking (position 0) and burning (maximum position) will always have `null` for `intervalLength` and `intervalUnit` since the stages do not influence [Assignment.availableAt]. Stages in between the unlocking and burning stages are the “reviewable” stages.
 * @property intervalLength The length of time added to the time of review registration, adjusted to the beginning of the hour.
 * @property intervalUnit The unit of time for the [intervalLength].
 * @property interval [kotlin.time.Duration] calculated from [intervalLength] and [intervalUnit].
 * @property position The position of the stage within the continuous order.
 */
@Serializable
data class Stage(
	@SerialName("interval")
	val intervalLength: Int?,
	val intervalUnit: IntervalUnit?,
	val position: Int,
) {
	@kotlinx.serialization.Transient
	val interval: Duration? = intervalLength?.let { intervalUnit?.toDuration(it) }
}

enum class IntervalUnit {
	@SerialName("seconds")
	Seconds,
	
	@SerialName("minutes")
	Minutes,
	
	@SerialName("hours")
	Hours,
	
	@SerialName("days")
	Days,
	
	@SerialName("weeks")
	Weeks;
	
	fun toDuration(length: Int): Duration = when (this) {
		Seconds -> length.seconds
		Minutes -> length.minutes
		Hours -> length.hours
		Days -> length.days
		Weeks -> length * 7.days
	}
}