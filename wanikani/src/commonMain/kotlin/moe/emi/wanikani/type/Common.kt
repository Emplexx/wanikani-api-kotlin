package moe.emi.wanikani.type

import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.format
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class ID(val value: Int) {
	override fun toString(): String = value.toString()
}

public typealias Present = Unit

public typealias Timestamp = kotlinx.datetime.Instant

fun Timestamp.toHttpDate(): String =
	DateTimeComponents.Formats.RFC_1123.format {
		setDateTimeOffset(this@toHttpDate, UtcOffset.ZERO)
		timeZoneId = "GMT"
	}

fun httpDateToTimestamp(date: String): Timestamp =
	DateTimeComponents.Formats.RFC_1123.parse(date).toInstantUsingOffset()