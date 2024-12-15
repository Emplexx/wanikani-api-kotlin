@file:Suppress("unused")

package moe.emi.wanikani

import io.ktor.client.HttpClient
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import moe.emi.wanikani.request.Request
import moe.emi.wanikani.type.Assignment
import moe.emi.wanikani.type.CreateReviewFor
import moe.emi.wanikani.type.CreateReviewResponse
import moe.emi.wanikani.type.ID
import moe.emi.wanikani.type.LevelProgression
import moe.emi.wanikani.type.Present
import moe.emi.wanikani.type.PresentationOrder
import moe.emi.wanikani.type.Report
import moe.emi.wanikani.type.Reset
import moe.emi.wanikani.type.Resource
import moe.emi.wanikani.type.ResourceSet
import moe.emi.wanikani.type.Review
import moe.emi.wanikani.type.ReviewStatistic
import moe.emi.wanikani.type.Srs
import moe.emi.wanikani.type.StudyMaterial
import moe.emi.wanikani.type.Subject
import moe.emi.wanikani.type.SubjectType
import moe.emi.wanikani.type.Subscription
import moe.emi.wanikani.type.Summary
import moe.emi.wanikani.type.Timestamp
import moe.emi.wanikani.type.User
import moe.emi.wanikani.type.VoiceActor

fun Wanikani(authToken: String, revision: Int = Wanikani.Revision20170710): Wanikani =
	WanikaniImpl(HttpClient().withWanikaniConfig(AuthProvider(authToken), revision))

fun Wanikani(config: WanikaniConfig.() -> Unit): Wanikani {
	val options = WanikaniConfig().apply(config)
	val client = options.httpClient ?: HttpClient()
	
	return WanikaniImpl(client.withWanikaniConfig(options.authProvider, options.revision))
}

interface Wanikani {
	
	// TODO add param docs https://docs.api.wanikani.com/20170710/#get-all-assignments
	/**
	 * Returns a collection of all assignments, ordered by ascending [Assignment.createdAt], 500 at a time.
	 *
	 * **Note:** It is possible for a user to have started an assignment for a subject that was later moved to a level above their current level. To exclude those assignments, filter by [levels] from 1 to the users current level
	 */
	fun getAssignments(
		ids: List<ID>? = null,
		levels: Iterable<Int>? = null,
		srsStages: Iterable<Int>? = null,
		subjectIds: List<ID>? = null,
		subjectTypes: List<SubjectType>? = null,
		
		availableBefore: Timestamp? = null,
		availableAfter: Timestamp? = null,
		
		immediatelyAvailableForLessons: Present? = null,
		immediatelyAvailableForReview: Present? = null,
		inReview: Present? = null,
		
		burned: Boolean? = null,
		hidden: Boolean? = null,
		started: Boolean? = null,
		unlocked: Boolean? = null,
		
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<Assignment>>
	
	/** Retrieves a specific assignment by its [id]. */
	fun getAssignment(id: ID): Request<Resource<Assignment>>
	
	/**
	 * Mark the [Assignment] as started, moving the assignment from the lessons queue to the review queue. Returns the updated assignment.
	 *
	 * ###### Expected Starting State
	 * | **Property** | **State** |
	 * | :----------- | :-------- |
	 * | [Assignment.level]	     | Must be less than or equal to the lowest value of [User]'s level and [Subscription.maxLevelGranted] |
	 * | [Assignment.srsStage]   | Must be equal to `0` |
	 * | [Assignment.startedAt]  | Must be equal to `null` |
	 * | [Assignment.unlockedAt] | Must not be `null` |
	 *
	 * ###### Updated Properties
	 * | **Property** | **New value** |
	 * | :----------- | :-------- |
	 * | [Assignment.availableAt] | [Timestamp] |
	 * | [Assignment.srsStage] | `0` |
	 * | [Assignment.startedAt] | [Timestamp] |
	 *
	 * @param id ID of the assignment to start
	 * @param startedAt Time when the assignment was started. If not set, the time the request is made will be used. [startedAt] must be greater than or equal to [Assignment.unlockedAt]
	 */
	fun startAssignment(
		id: ID,
		startedAt: Timestamp? = null,
	): Request<Resource<Assignment>>
	
	fun getLevelProgressions(
		ids: List<ID>? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<LevelProgression>>
	
	fun getLevelProgression(id: ID): Request<Resource<LevelProgression>>
	
	fun getResets(
		ids: List<ID>? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<Reset>>
	
	fun getReset(id: ID): Request<Resource<Reset>>
	
	/** Retrieves a specific review by its [id]. */
	fun getReview(id: ID): Request<Resource<Review>>
	
	/**
	 * Creates a review for a specific [Assignment], or a related [Subject], specified via [CreateReviewFor].
	 *
	 * Some criteria must be met in order for a review to be created: [Assignment.availableAt] must be not `null` and in the past.
	 *
	 * When a review is registered, the associated [Assignment] and [ReviewStatistic] are both updated. These are returned in [CreateReviewResponse] under [CreateReviewResponse.resourcesUpdated].
	 *
	 * @param id Unique identified of the [Assignment] or the [Subject], either [CreateReviewFor.Assignment] or [CreateReviewFor.Subject]
	 * @param incorrectMeaningAnswers Must be zero or a positive number. This is the number of times the meaning was answered **incorrectly**.
	 * @param incorrectReadingAnswers Must be zero or a positive number. This is the number of times the reading was answered **incorrectly**. Note that subjects with a `type` or `radical` do not quiz on readings. Thus, set this value to `0`.
	 * @param createdAt Timestamp when the review was completed. Defaults to the time of the request if omitted from the request body. Must be in the past, but after [Assignment.availableAt].
	 */
	fun createReview(
		id: CreateReviewFor,
		incorrectMeaningAnswers: Int,
		incorrectReadingAnswers: Int,
		createdAt: Timestamp? = null,
	): Request<CreateReviewResponse>
	
	/**
	 * Returns a collection of all review statistics, ordered by ascending [ReviewStatistic.createdAt], 500 at a time.
	 * @param hidden Filter by matching [ReviewStatistic.hidden] value
	 * @param ids Filter by the ID of [ReviewStatistic]
	 * @param subjectIds Filter by [ReviewStatistic.subjectId]
	 */
	fun getReviewStatistics(
		hidden: Boolean? = null,
		ids: List<ID>? = null,
		subjectIds: List<ID>? = null,
		subjectTypes: List<SubjectType>? = null,
		percentagesGreaterThan: Int? = null,
		percentagesLessThan: Int? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<ReviewStatistic>>
	
	/** Retrieves a specific review statistic by its [id]. */
	fun getReviewStatistic(id: ID): Request<Resource<ReviewStatistic>>
	
	fun getSrses(
		ids: List<ID>? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<Srs>>
	
	fun getSrs(id: ID): Request<Resource<Srs>>
	
	/**
	 * Returns a collection of all study materials, ordered by ascending [StudyMaterial.createdAt], 500 at a time.
	 */
	fun getStudyMaterials(
		hidden: Boolean? = null,
		ids: List<ID>? = null,
		subjectIds: List<ID>? = null,
		subjectTypes: List<SubjectType>? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<StudyMaterial>>
	
	fun getStudyMaterial(id: ID): Request<Resource<StudyMaterial>>
	
	/**
	 * Creates a study material for a specific [subjectId].
	 *
	 * The owner of the api key can only create one [StudyMaterial] per [subjectId].
	 */
	fun createStudyMaterial(
		subjectId: ID,
		meaningNote: String? = null,
		readingNote: String? = null,
		meaningSynonyms: List<String>? = null,
	): Request<Resource<StudyMaterial>>
	
	/**
	 * Updates a study material for a specific [id].
	 */
	fun updateStudyMaterial(
		id: ID,
		meaningNote: String? = null,
		readingNote: String? = null,
		meaningSynonyms: List<String>? = null,
	): Request<Resource<StudyMaterial>>
	
	fun getSubjects(
		ids: List<ID>? = null,
		types: List<String>? = null,
		slugs: List<String>? = null,
		levels: Iterable<Int>? = null,
		hidden: Boolean? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<Subject>>
	
	fun getSummary(): Request<Report<Summary>>
	
	fun getUser(): Request<Report<User>>
	
	fun updateUser(
		extraStudyAutoplayAudio: Boolean? = null,
		lessonsAutoplayAudio: Boolean? = null,
		lessonsBatchSize: Int? = null,
		reviewsAutoplayAudio: Boolean? = null,
		reviewsDisplaySrsIndicator: Boolean? = null,
		reviewsPresentationOrder: PresentationOrder? = null,
	): Request<Report<User>>
	
	
	fun getVoiceActors(
		ids: List<ID>? = null,
		updatedAfter: Timestamp? = null,
	): Request<ResourceSet<VoiceActor>>
	
	fun getVoiceActor(id: ID): Request<Resource<VoiceActor>>
	
	fun <A> getNextPage(url: String, typeInfo: TypeInfo): Request<ResourceSet<A>>
	
	fun <A> getPreviousPage(url: String, typeInfo: TypeInfo): Request<ResourceSet<A>>
	
	companion object {
		const val Revision20170710 = 20170710
	}
	
}

inline fun <reified A> Wanikani.getNextPage(url: String): Request<ResourceSet<A>> =
	getNextPage(url, typeInfo<ResourceSet<A>>())

inline fun <reified A> Wanikani.getPreviousPage(url: String): Request<ResourceSet<A>> =
	getPreviousPage(url, typeInfo<ResourceSet<A>>())

inline fun <reified A> Wanikani.getNextPage(collection: ResourceSet<A>): Request<ResourceSet<A>>? =
	collection.pages.nextUrl?.let { getNextPage(it) }

inline fun <reified A> Wanikani.getPreviousPage(collection: ResourceSet<A>): Request<ResourceSet<A>>? =
	collection.pages.previousUrl?.let { getPreviousPage(it) }

class WanikaniConfig {
	var authProvider: AuthProvider = AuthProvider.None
	var revision: Int = Wanikani.Revision20170710
	var httpClient: HttpClient? = null
}