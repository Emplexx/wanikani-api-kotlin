package moe.emi.wanikani.type

import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import java.util.Date

@JvmInline
@Serializable
value class ID(val value: Int) {
	override fun toString(): String = value.toString()
}

typealias Present = Unit

typealias Timestamp = kotlinx.datetime.Instant

fun Timestamp.toJavaDate(): Date = Date.from(toJavaInstant())