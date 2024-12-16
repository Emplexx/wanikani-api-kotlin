package moe.emi.wanikani.serialization

import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import moe.emi.wanikani.type.KanaVocabulary
import moe.emi.wanikani.type.Kanji
import moe.emi.wanikani.type.Radical
import moe.emi.wanikani.type.Subject
import moe.emi.wanikani.type.Vocabulary

object SubjectSerializer : JsonContentPolymorphicSerializer<Subject>(Subject::class) {
	override fun selectDeserializer(element: JsonElement) = when {
		
		// only Kanji & Vocab have "readings"
		"readings" in element.jsonObject -> when {
			"visuallySimilar_subject_ids" in element.jsonObject
					|| "amalgamation_subject_ids" in element.jsonObject -> Kanji.serializer()
			
			else -> Vocabulary.serializer()
		}
		
		"amalgamation_subject_ids" in element.jsonObject
				|| "character_images" in element.jsonObject -> Radical.serializer()
		
		else -> KanaVocabulary.serializer()
	}
}