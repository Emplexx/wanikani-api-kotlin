package moe.emi.wanikani

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import moe.emi.wanikani.request.Request
import moe.emi.wanikani.request.getRequest
import moe.emi.wanikani.request.parameters
import moe.emi.wanikani.request.postRequest
import moe.emi.wanikani.request.putRequest
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
import moe.emi.wanikani.type.Summary
import moe.emi.wanikani.type.Timestamp
import moe.emi.wanikani.type.User
import moe.emi.wanikani.type.VoiceActor
import moe.emi.wanikani.type.request.CreateReviewRequest

class WanikaniImpl(private val client: HttpClient) : Wanikani {
	
	override fun getAssignments(
		ids: List<ID>?,
		levels: Iterable<Int>?,
		srsStages: Iterable<Int>?,
		subjectIds: List<ID>?,
		subjectTypes: List<SubjectType>?,
		
		availableBefore: Timestamp?,
		availableAfter: Timestamp?,
		
		immediatelyAvailableForLessons: Present?,
		immediatelyAvailableForReview: Present?,
		inReview: Present?,
		
		burned: Boolean?,
		hidden: Boolean?,
		started: Boolean?,
		unlocked: Boolean?,
		
		updatedAfter: Timestamp?,
	): Request<ResourceSet<Assignment>> =
		client.getRequest("assignments") {
			parameters(
				"ids" to ids,
				"levels" to levels,
				"srs_stages" to srsStages,
				"subject_ids" to subjectIds,
				"subject_types" to subjectTypes,
				
				"available_before" to availableBefore,
				"available_after" to availableAfter,
				
				"immediately_available_for_lessons" to immediatelyAvailableForLessons,
				"immediately_available_for_review" to immediatelyAvailableForReview,
				"in_review" to inReview,
				
				"burned" to burned,
				"hidden" to hidden,
				"started" to started,
				"unlocked" to unlocked,
				
				"updated_after" to updatedAfter
			)
		}
	
	override fun getAssignment(id: ID): Request<Resource<Assignment>> =
		client.getRequest("assignments/$id")
	
	override fun startAssignment(
		id: ID,
		startedAt: Timestamp?,
	): Request<Resource<Assignment>> =
		client.putRequest("assignments/$id/start") {
			parameters("started_at" to startedAt)
		}
	
	override fun getLevelProgressions(
		ids: List<ID>?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<LevelProgression>> =
		client.getRequest("level_progressions") {
			parameters(
				"ids" to ids,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getLevelProgression(id: ID): Request<Resource<LevelProgression>> =
		client.getRequest("level_progressions/$id")
	
	override fun getResets(
		ids: List<ID>?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<Reset>> =
		client.getRequest("resets") {
			parameters(
				"ids" to ids,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getReset(id: ID): Request<Resource<Reset>> =
		client.getRequest("resets/$id")
	
	override fun getReview(id: ID): Request<Resource<Review>> = client.getRequest("reviews/$id")
	
	override fun createReview(
		id: CreateReviewFor,
		incorrectMeaningAnswers: Int,
		incorrectReadingAnswers: Int,
		createdAt: Timestamp?,
	): Request<CreateReviewResponse> = run {
		client.postRequest("reviews") {
			
			val review = CreateReviewRequest.Review(
				id.assignmentId,
				id.subjectId,
				incorrectMeaningAnswers,
				incorrectReadingAnswers,
				createdAt
			)
			
			contentType(ContentType.Application.Json)
			setBody(CreateReviewRequest(review))
		}
	}
	
	override fun getReviewStatistics(
		hidden: Boolean?,
		ids: List<ID>?,
		subjectIds: List<ID>?,
		subjectTypes: List<SubjectType>?,
		percentagesGreaterThan: Int?,
		percentagesLessThan: Int?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<ReviewStatistic>> =
		client.getRequest("review_statistics") {
			parameters(
				"hidden" to hidden,
				"ids" to ids,
				"subject_ids" to subjectIds,
				"subject_types" to subjectTypes,
				"percentages_greater_than" to percentagesGreaterThan,
				"percentages_less_than" to percentagesLessThan,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getReviewStatistic(id: ID): Request<Resource<ReviewStatistic>> =
		client.getRequest("review_statistics/$id")
	
	override fun getSrses(
		ids: List<ID>?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<Srs>> =
		client.getRequest("spaced_repetition_systems") {
			parameters(
				"ids" to ids,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getSrs(id: ID): Request<Resource<Srs>> =
		client.getRequest("spaced_repetition_systems/$id")
	
	override fun getStudyMaterials(
		hidden: Boolean?,
		ids: List<ID>?,
		subjectIds: List<ID>?,
		subjectTypes: List<SubjectType>?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<StudyMaterial>> =
		client.getRequest("study_materials") {
			parameters(
				"hidden" to hidden,
				"ids" to ids,
				"subject_ids" to subjectIds,
				"subject_types" to subjectTypes,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getStudyMaterial(id: ID): Request<Resource<StudyMaterial>> =
		client.getRequest("study_materials/$id")
	
	override fun createStudyMaterial(
		subjectId: ID,
		meaningNote: String?,
		readingNote: String?,
		meaningSynonyms: List<String>?,
	): Request<Resource<StudyMaterial>> =
		client.postRequest("study_materials") {
			parameters(
				"subject_id" to subjectId,
				"meaning_note" to meaningNote,
				"reading_note" to readingNote,
				"meaning_synonyms" to meaningSynonyms,
			)
		}
	
	override fun updateStudyMaterial(
		id: ID,
		meaningNote: String?,
		readingNote: String?,
		meaningSynonyms: List<String>?,
	): Request<Resource<StudyMaterial>> =
		client.putRequest("study_materials/$id") {
			parameters(
				"meaning_note" to meaningNote,
				"reading_note" to readingNote,
				"meaning_synonyms" to meaningSynonyms,
			)
		}
	
	override fun getSubjects(
		ids: List<ID>?,
		types: List<String>?,
		slugs: List<String>?,
		levels: Iterable<Int>?,
		hidden: Boolean?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<Subject>> =
		client.getRequest("subjects") {
			parameters(
				"ids" to ids,
				"types" to types,
				"slugs" to slugs,
				"levels" to levels,
				"hidden" to hidden,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getSummary(): Request<Report<Summary>> =
		client.getRequest("summary")
	
	override fun getUser(): Request<Report<User>> =
		client.getRequest("user")
	
	override fun updateUser(
		extraStudyAutoplayAudio: Boolean?,
		lessonsAutoplayAudio: Boolean?,
		lessonsBatchSize: Int?,
		reviewsAutoplayAudio: Boolean?,
		reviewsDisplaySrsIndicator: Boolean?,
		reviewsPresentationOrder: PresentationOrder?,
	): Request<Report<User>> =
		client.putRequest("user") {
			parameters(
				"extra_study_autoplay_audio" to extraStudyAutoplayAudio,
				"lessons_autoplay_audio" to lessonsAutoplayAudio,
				"lessons_batch_size" to lessonsBatchSize,
				"reviews_autoplay_audio" to reviewsAutoplayAudio,
				"reviews_display_srs_indicator" to reviewsDisplaySrsIndicator,
				"reviews_presentation_order" to reviewsPresentationOrder,
			)
		}
	
	override fun getVoiceActors(
		ids: List<ID>?,
		updatedAfter: Timestamp?,
	): Request<ResourceSet<VoiceActor>> =
		client.getRequest("voice_actors") {
			parameters(
				"ids" to ids,
				"updated_after" to updatedAfter
			)
		}
	
	override fun getVoiceActor(id: ID): Request<Resource<VoiceActor>> =
		client.getRequest("voice_actors/$id")
	
	override fun <A> getNextPage(url: String, typeInfo: TypeInfo): Request<ResourceSet<A>> =
		client.getRequest(typeInfo, url)
	
	override fun <A> getPreviousPage(url: String, typeInfo: TypeInfo): Request<ResourceSet<A>> =
		client.getRequest(typeInfo, url)
}