package moe.emi.wanikani.type

import kotlinx.serialization.Serializable

// TODO add docs
@Serializable
data class Reset(
	val originalLevel: Int,
	val targetLevel: Int,
	val createdAt: Timestamp,
	val confirmedAt: Timestamp?,
)