package moe.emi.wanikani.type.body

import kotlinx.serialization.Serializable
import moe.emi.wanikani.type.ID

@Serializable
data class CreateStudyMaterialRequest(
	val studyMaterial: StudyMaterial
) {
	
	@Serializable
	data class StudyMaterial(
		val subjectId: ID,
		val meaningNote: String? = null,
		val readingNote: String? = null,
		val meaningSynonyms: List<String>? = null,
	)
}
