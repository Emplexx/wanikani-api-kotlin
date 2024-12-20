package moe.emi.wanikani.type.body

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStudyMaterialRequest(
	val studyMaterial: StudyMaterial
) {
	
	@Serializable
	data class StudyMaterial(
		val meaningNote: String? = null,
		val readingNote: String? = null,
		val meaningSynonyms: List<String>? = null,
	)
}
