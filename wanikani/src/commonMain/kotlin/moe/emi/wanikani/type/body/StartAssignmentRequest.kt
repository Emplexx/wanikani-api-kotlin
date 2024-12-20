package moe.emi.wanikani.type.body

import kotlinx.serialization.Serializable
import moe.emi.wanikani.type.Timestamp

@Serializable
data class StartAssignmentRequest(
	val assignment: Assignment
) {
	
	@Serializable
	data class Assignment(
		val startedAt: Timestamp? = null,
	)
}
