package moe.emi.wanikani.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Generic representation of a response from the WaniKani API. You most likely won't ever need to use this directly.
 * @property object The kind of object returned. See [ObjectType].
 * @property url The URL of the request. For collections, that will contain all the filters and options you've passed to the API. Resources have a single URL and don't need to be filtered, so the URL will be the same in both resource and collection responses.
 * @property dataUpdatedAt For collections, this is the timestamp of the most recently updated resource in the specified scope and is not limited by pagination. If no resources were returned for the specified scope, then this will be null. For a resource, then this is the last time that particular resource was updated.
 * @property data For collections, this is going to be the resources returned by the specified scope. For resources, these are the attributes that are specific to that particular instance and kind of resource.
 */
@Serializable
sealed interface WanikaniResponse<A> {
	val `object`: ObjectType
	val url: String
	val dataUpdatedAt: Timestamp?
	val data: A
}

enum class ObjectType {
	@SerialName("collection")
	Collection,
	
	@SerialName("report")
	Report,
	
	@SerialName("assignment")
	Assignment,
	
	@SerialName("kana_vocabulary")
	KanaVocabulary,
	
	@SerialName("kanji")
	Kanji,
	
	@SerialName("level_progression")
	LevelProgression,
	
	@SerialName("radical")
	Radical,
	
	@SerialName("reset")
	Reset,
	
	@SerialName("review_statistic")
	ReviewStatistic,
	
	@SerialName("review")
	Review,
	
	@SerialName("spaced_repetition_system")
	SpacedRepetitionSystem,
	
	@SerialName("study_material")
	StudyMaterial,
	
	@SerialName("user")
	User,
	
	@SerialName("vocabulary")
	Vocabulary,
	
	@SerialName("voice_actor")
	VoiceActor,
}

/**
 * A WaniKani Resource response.
 * Singular resource endpoints deliver information about a single entity, such as an assignment or subject.
 */
@Serializable
data class Resource<A>(
	val id: ID,
	
	override val `object`: ObjectType,
	override val url: String,
	override val dataUpdatedAt: Timestamp,
	override val data: A
) : WanikaniResponse<A>

/**
 * A WaniKani Collection response.
 * Collections contain summary data about a bunch of resources, and also include each of the resources.
 *
 * To avoid conflict with Kotlin's [kotlin.collections.Collection], this is named [ResourceSet].
 *
 * This may actually use [Set] under the hood in the future since the resources in the collection likely never repeat.
 */
@Serializable
data class ResourceSet<A>(
	override val `object`: ObjectType,
	override val url: String,
	override val dataUpdatedAt: Timestamp?,
	override val data: List<Resource<A>>,
	
	val pages: Pages,
	val totalCount: Int
) : WanikaniResponse<List<Resource<A>>>

@Serializable
data class Pages(
	val nextUrl: String?,
	val previousUrl: String?,
	val perPage: Int
)

@Serializable
data class Report<A>(
	override val `object`: ObjectType,
	override val url: String,
	override val dataUpdatedAt: Timestamp,
	override val data: A
) : WanikaniResponse<A>