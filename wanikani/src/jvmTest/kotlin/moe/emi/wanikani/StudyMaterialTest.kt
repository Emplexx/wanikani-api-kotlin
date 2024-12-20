package moe.emi.wanikani

import kotlinx.coroutines.test.runTest
import moe.emi.wanikani.response.execute
import moe.emi.wanikani.type.ID
import kotlin.test.Test

class StudyMaterialTest {
	
	@Test
	fun testGetStudyMaterial() = runTest {
		getTestClient()
			.getStudyMaterials(subjectIds = listOf(ID(441)))
			.execute()
			.getOrThrow().prettyPrintJson()
	}
	
	@Test
	fun testCreateStudyMaterial() = runTest {
		getTestClient()
			.createStudyMaterial(ID(441), "test", "test", listOf("test"))
			.execute()
			.getOrThrow().prettyPrintJson()
	}
	
	@Test
	fun testUpdateStudyMaterial() = runTest {
		getTestClient()
			.updateStudyMaterial(ID(16516292), "test2", "test2", listOf("test2"))
			.execute()
			.getOrThrow().prettyPrintJson()
	}
	
}