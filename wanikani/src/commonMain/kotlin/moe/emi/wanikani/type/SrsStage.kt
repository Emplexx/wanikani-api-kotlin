package moe.emi.wanikani.type

import kotlinx.serialization.Serializable
import moe.emi.wanikani.serialization.SrsStageSerializer
import moe.emi.wanikani.serialization.SrsStageStartedOrBurnedSerializer
import moe.emi.wanikani.serialization.SrsStageStartedSerializer

/**
 * Represents a stage in the WaniKani SRS system. Stages range from 0 to 9:
 * - 0 is [Unlocked], stage for subjects that are available as lessons
 * - 1-8 are stages for subjects that have been started by the user, and are represented via sub-interface [Started]
 * - 9 is [Burned]
 *
 * For more information, see [WaniKani's SRS Stages](https://knowledge.wanikani.com/wanikani/srs-stages/)
 */
@Serializable(SrsStageSerializer::class)
sealed interface SrsStage {
	
	val value: Int
	
	/**
	 * Represents stages 1-9 in the WaniKani SRS system - [Started] stages and the [Burned] stage.
	 */
	@Serializable(SrsStageStartedOrBurnedSerializer::class)
	sealed interface StartedOrBurned : SrsStage
	
	/**
	 * Represents stages 1-8 in the WaniKani SRS system.
	 */
	@Serializable(SrsStageStartedSerializer::class)
	sealed interface Started : StartedOrBurned, SrsStage
	
	data object Unlocked : SrsStage { override val value: Int = 0; override fun toString() = value.toString() }
	
	data object Apprentice1 : Started { override val value: Int = 1; override fun toString() = value.toString() }
	data object Apprentice2 : Started { override val value: Int = 2; override fun toString() = value.toString() }
	data object Apprentice3 : Started { override val value: Int = 3; override fun toString() = value.toString() }
	data object Apprentice4 : Started { override val value: Int = 4; override fun toString() = value.toString() }
	
	data object Guru1 : Started { override val value: Int = 5; override fun toString() = value.toString() }
	data object Guru2 : Started { override val value: Int = 6; override fun toString() = value.toString() }
	data object Master : Started { override val value: Int = 7; override fun toString() = value.toString() }
	data object Enlightened : Started { override val value: Int = 8; override fun toString() = value.toString() }
	
	data object Burned : StartedOrBurned { override val value: Int = 9; override fun toString() = value.toString() }
	
	companion object {
		
		operator fun invoke(value: Int): SrsStage = when (value) {
			0 -> Unlocked
			1 -> Apprentice1
			2 -> Apprentice2
			3 -> Apprentice3
			4 -> Apprentice4
			5 -> Guru1
			6 -> Guru2
			7 -> Master
			8 -> Enlightened
			9 -> Burned
			else -> throw IllegalArgumentException("Invalid SRS stage value: $value")
		}
		
	}
	
}

fun srsStages(values: Iterable<Int>) = values.map { SrsStage(it) }

fun srsStages(vararg values: Int) = values.map { SrsStage(it) }