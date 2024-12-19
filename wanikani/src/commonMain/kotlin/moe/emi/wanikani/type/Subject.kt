@file:Suppress("unused")

package moe.emi.wanikani.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.emi.wanikani.serialization.SubjectSerializer

/**
 * @property characters The UTF-8 characters for the subject, including kanji and hiragana.
 * @property createdAt Timestamp when the subject was created.
 * @property documentUrl A URL pointing to the page on wanikani.com that provides detailed information about this subject.
 * @property hiddenAt Timestamp when the subject was hidden, indicating associated assignments will no longer appear in lessons or reviews and that the subject page is no longer visible on wanikani.com.
 * @property lessonPosition The position that the subject appears in lessons. Note that the value is scoped to the level of the subject, so there are duplicate values across levels.
 * @property level The level of the subject, from `1` to `60`.
 * @property meaningMnemonic The subject's meaning mnemonic.
 * @property meanings The subject [Meaning]s.
 * @property slug The string that is used when generating the document URL for the subject. Radicals use their meaning, downcased. Kanji and vocabulary use their characters.
 * @property spacedRepetitionSystemId Unique identifier of the associated [Srs].
 */
@Serializable(with = SubjectSerializer::class)
sealed interface Subject {
	val auxiliaryMeanings: List<AuxiliaryMeaning>
	val characters: String?
	val createdAt: Timestamp
	val documentUrl: String
	val hiddenAt: Timestamp?
	val lessonPosition: Int
	val level: Int
	val meaningMnemonic: String
	val meanings: List<Meaning>
	val slug: String
	val spacedRepetitionSystemId: ID
}

@Serializable
enum class SubjectType(val value: String) {
	@SerialName("kana_vocabulary")
	KanaVocabulary("kana_vocabulary"),
	
	@SerialName("kanji")
	Kanji("kanji"),
	
	@SerialName("radical")
	Radical("radical"),
	
	@SerialName("vocabulary")
	Vocabulary("vocabulary")
}

/**
 * @property meaning A singular subject meaning.
 * @property primary Indicates priority in the WaniKani system.
 * @property acceptedAnswer Indicates if the meaning is used to evaluate user input for correctness.
 */
@Serializable
data class Meaning(
	val meaning: String,
	val primary: Boolean,
	val acceptedAnswer: Boolean
)

/**
 * @property meaning A singular subject meaning.
 * @property type Either whitelist or blacklist. When evaluating user input, whitelisted meanings are used to match for correctness. Blacklisted meanings are used to match for incorrectness.
 */
@Serializable
data class AuxiliaryMeaning(
	val meaning: String,
	val type: Type
) {
	
	// TODO does this (de)serialize?
	enum class Type {
		@SerialName("whitelist")
		Whitelist,
		
		@SerialName("blacklist")
		Blacklist
	}
}

/**
 * @property amalgamationSubjectIds An array of numeric identifiers for the [Kanji] that have the radical as a component.
 * @property characters Unlike kanji and vocabulary, radicals can have a null value for characters. Not all radicals have a UTF entry, so the radical must be visually represented with an image instead.
 * @property characterImages A collection of images of the radical.
 */
@Serializable
data class Radical(
	override val characters: String?,
	override val level: Int,
	override val auxiliaryMeanings: List<AuxiliaryMeaning>,
	override val createdAt: Timestamp,
	override val documentUrl: String,
	override val hiddenAt: Timestamp?,
	override val lessonPosition: Int,
	override val meaningMnemonic: String,
	override val meanings: List<Meaning>,
	override val slug: String,
	override val spacedRepetitionSystemId: ID,
	
	val amalgamationSubjectIds: List<ID>,
	val characterImages: List<CharacterImage>
) : Subject

@Serializable
data class CharacterImage(
	val url: String,
	val contentType: String,
//	val metadata: Metadata,
) {
	// TODO add content types because the API lied when it said "i only deliver image/svg+xml"
	//  "content_type":"image/png"
	//  {"color":"#000000","dimensions":"1024x1024","style_name":"original"}
	//  "content_type":"image/svg+xml"
	//  {"inline_styles":true}
	@Serializable
	data class Metadata(
		val inlineStyles: Boolean = true,
	)
}


/**
 * @property amalgamationSubjectIds An array of numeric identifiers for the [Vocabulary] that have the kanji as a component.
 * @property componentSubjectIds An array of numeric identifiers for the [Radical]s that make up this kanji. Note that these are the subjects that must have passed assignments in order to unlock this subject's assignment.
 * @property visuallySimilarSubjectIds An array of numeric identifiers for kanji which are visually similar to the kanji in question.
 */
@Serializable
data class Kanji(
	override val characters: String,
	override val level: Int,
	override val auxiliaryMeanings: List<AuxiliaryMeaning>,
	override val createdAt: Timestamp,
	override val documentUrl: String,
	override val hiddenAt: Timestamp?,
	override val lessonPosition: Int,
	override val meaningMnemonic: String,
	override val meanings: List<Meaning>,
	override val slug: String,
	override val spacedRepetitionSystemId: ID,
	
	val amalgamationSubjectIds: List<ID>,
	val componentSubjectIds: List<ID>,
	val meaningHint: String?,
	val readingHint: String?,
	val readingMnemonic: String,
	val readings: List<Reading>,
	val visuallySimilarSubjectIds: List<ID>
) : Subject {
	/**
	 * @property reading A singular subject reading.
	 * @property primary Indicates priority in the WaniKani system.
	 * @property acceptedAnswer Indicates if the reading is used to evaluate user input for correctness.
	 * @property type: The kanji reading's classification: kunyomi, nanori, or onyomi.
	 */
	@Serializable
	data class Reading(
		val reading: String,
		val primary: Boolean,
		val acceptedAnswer: Boolean,
		val type: Type
	) {
		enum class Type {
			@SerialName("kunyomi")
			Kunyomi,
			
			@SerialName("nanori")
			Nanori,
			
			@SerialName("onyomi")
			Onyomi
		}
	}
}


/**
 * @property componentSubjectIds An array of numeric identifiers for the [Kanji] that make up this vocabulary. Note that these are the subjects that must be have passed assignments in order to unlock this subject's assignment.
 */
@Serializable
data class Vocabulary(
	override val characters: String,
	override val level: Int,
	override val auxiliaryMeanings: List<AuxiliaryMeaning>,
	override val createdAt: Timestamp,
	override val documentUrl: String,
	override val hiddenAt: Timestamp?,
	override val lessonPosition: Int,
	override val meaningMnemonic: String,
	override val meanings: List<Meaning>,
	override val slug: String,
	override val spacedRepetitionSystemId: ID,
	
	val componentSubjectIds: List<ID>,
	val contextSentences: List<ContextSentence>,
	val partsOfSpeech: List<String>,
	val pronunciationAudios: List<PronunciationAudio>,
	val readings: List<Reading>,
	val readingMnemonic: String
) : Subject {
	/**
	 * @property acceptedAnswer Indicates if the reading is used to evaluate user input for correctness.
	 * @property primary Indicates priority in the WaniKani system.
	 */
	@Serializable
	data class Reading(
		val acceptedAnswer: Boolean,
		val primary: Boolean,
		val reading: String,
	)
}

/**
 * @property ja Japanese context sentence.
 * @property en The English translation of the sentence.
 */
@Serializable
data class ContextSentence(val ja: String, val en: String)

/**
 * @property contentType The content type of the audio. Currently the API delivers `audio/mpeg` and `audio/ogg`.
 */
@Serializable
data class PronunciationAudio(
	val url: String,
	val contentType: String,
	val metadata: Metadata
) {
	@Serializable
	data class Metadata(
		val gender: String,
		val sourceId: Int,
		val pronunciation: String,
		val voiceActorId: Int,
		val voiceActorName: String,
		val voiceDescription: String
	)
}

@Serializable
data class KanaVocabulary(
	override val characters: String,
	override val level: Int,
	override val auxiliaryMeanings: List<AuxiliaryMeaning>,
	override val createdAt: Timestamp,
	override val documentUrl: String,
	override val hiddenAt: Timestamp?,
	override val lessonPosition: Int,
	override val meaningMnemonic: String,
	override val meanings: List<Meaning>,
	override val slug: String,
	override val spacedRepetitionSystemId: ID,
	
	val contextSentences: List<ContextSentence>,
	val partsOfSpeech: List<String>,
	val pronunciationAudios: List<PronunciationAudio>,
) : Subject