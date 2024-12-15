package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

/**
 * Study materials store user-specific notes and synonyms for a given subject. The records are created as soon as the user enters any study information.
 * @property meaningNote Free form note related to the meaning(s) of the associated subject.
 * @property meaningSynonyms Synonyms for the meaning of the subject. These are used as additional correct answers during reviews.
 * @property readingNote Free form note related to the reading(s) of the associated subject.
 * @property subjectId Unique identifier of the associated [Subject].
 * @property subjectType Type of the associated [Subject].
 * @property hidden Indicates if the associated subject has been hidden, preventing it from appearing in lessons or reviews.
 * @property createdAt Timestamp when the study material was created.
 */
@Serializable
data class StudyMaterial(
	val meaningNote: String,
	val meaningSynonyms: List<String>,
	val readingNote: String,
	val subjectId: ID,
	val subjectType: SubjectType,
	val hidden: Boolean,
	val createdAt: Timestamp,
)