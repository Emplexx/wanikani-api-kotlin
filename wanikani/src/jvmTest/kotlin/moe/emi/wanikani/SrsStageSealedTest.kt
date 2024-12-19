package moe.emi.wanikani

import io.kotest.matchers.collections.shouldBeIn
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import moe.emi.wanikani.response.execute
import moe.emi.wanikani.serialization.WanikaniJson
import moe.emi.wanikani.type.Review
import moe.emi.wanikani.type.SrsStage
import moe.emi.wanikani.type.srsStages
import kotlin.test.Test

class SrsStageSealedTest {
	
	val client = getTestClient()
	
	@Test
	fun testSerializeSrsStages() {
		val sampleJson = """
			{
			    "assignment_id": 459469181,
			    "subject_id": 7615,
			    "spaced_repetition_system_id": 2,
			    "created_at": "2024-12-19T23:13:47.502586Z",
			    "starting_srs_stage": 6,
			    "ending_srs_stage": 2,
			    "incorrect_meaning_answers": 2,
			    "incorrect_reading_answers": 2
			}
		""".trimIndent()
		
		val review = WanikaniJson.decodeFromString<Review>(sampleJson)
		
		srsStages(1..8).forEach {
			val copy = review.copy(startingSrsStage = it as SrsStage.Started)
			WanikaniJson.encodeToString(copy)
		}
		
		srsStages(1..9).forEach {
			val copy = review.copy(endingSrsStage = it as SrsStage.StartedOrBurned)
			WanikaniJson.encodeToString(copy)
		}
		
	}
	
	@Test
	fun testQueryParams() = runTest {
		suspend fun testRange(range: List<SrsStage>) {
			client.getAssignments(
				srsStages = range,
				availableBefore = Clock.System.now(),
			).execute().getOrThrow().data.onEach {
				it.data.srsStage shouldBeIn range
			}
		}
		
		testRange(srsStages(1..8))
		testRange(srsStages(1..2))
		testRange(srsStages(1))
		testRange(srsStages(0))
		testRange(srsStages(9))
	}
	
	@Test
	fun getAssignment() = runTest {
		client.getAssignments(
			srsStages = srsStages(9),
			availableBefore = Clock.System.now(),
		).execute().getOrThrow().prettyPrintJson()
	}
	
}