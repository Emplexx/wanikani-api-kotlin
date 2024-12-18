package moe.emi.wanikani.type

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.emi.wanikani.type.PresentationOrder.*

/**
 * The user summary returns basic information for the user making the API request, identified by their API key.
 * @property currentVacationStartedAt If the user is on vacation, this will be the timestamp of when that vacation started. If the user is not on vacation, this is null.
 */
@Serializable
data class User(
	val username: String,
	val level: Int,
	val startedAt: Instant,
	val currentVacationStartedAt: Instant? = null,
	val preferences: Preferences,
	val subscription: Subscription,
	val profileUrl: String,
)

/**
 * @property extraStudyAutoplayAudio Automatically play pronunciation audio for vocabulary during extra study.
 * @property lessonsAutoplayAudio Automatically play pronunciation audio for vocabulary during lessons.
 * @property lessonsBatchSize Number of subjects introduced to the user during lessons before quizzing.
 * @property reviewsAutoplayAudio Automatically play pronunciation audio for vocabulary during reviews.
 * @property reviewsDisplaySrsIndicator Toggle for display SRS change indicator after a subject has been completely answered during review.
 * @property reviewsPresentationOrder The order in which reviews are presented. The default (and best experience) is [PresentationOrder.Shuffled].
 */
@Serializable
data class Preferences(
	val extraStudyAutoplayAudio: Boolean,
	
	val lessonsAutoplayAudio: Boolean,
	val lessonsBatchSize: Int,
	
	val reviewsAutoplayAudio: Boolean,
	val reviewsDisplaySrsIndicator: Boolean,
	val reviewsPresentationOrder: PresentationOrder,
	
	@Deprecated("This is a deprecated user preference. It will always return 1 and cannot be set. It exists only to ensure existing consumers of this API don't break.")
	val defaultVoiceActorId: Int,
	@Deprecated("This is a deprecated user preference. It always returns ascending_level_then_subject. Setting this preference will do nothing. It exists only to ensure existing consumers of this API don't break.")
	val lessonsPresentationOrder: String,
)

/**
 * The order in which reviews are presented. The default (and best experience) is [Shuffled].
 */
enum class PresentationOrder {
	@SerialName("shuffled")
	Shuffled,
	
	@SerialName("lower_levels_first")
	LowerLevelsFirst
}

/**
 * @property active Whether or not the user currently has a paid subscription.
 * @property maxLevelGranted The maximum level of content accessible to the user for lessons, reviews, and content review. For unsubscribed/free users, the maximum level is 3. For subscribed users, this is 60. **Any application that uses data from the WaniKani API must respect these access limits.**
 * @property periodEndsAt The date when the user's subscription period ends. If the user has subscription type lifetime or free then the value is `null`.
 * @property type The type of subscription the user has.
 */
@Serializable
data class Subscription(
	val active: Boolean,
	val maxLevelGranted: Int,
	val periodEndsAt: Instant?,
	val type: Type
) {
	enum class Type {
		@SerialName("free")
		Free,
		
		@SerialName("recurring")
		Recurring,
		
		@SerialName("lifetime")
		Lifetime
	}
}