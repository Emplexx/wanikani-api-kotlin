package moe.emi.wanikani.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import moe.emi.wanikani.type.SrsStage

inline fun <reified T : SrsStage> srsStageSerializer(
	crossinline create: (Int) -> T
) = object : KSerializer<T> {
	
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SrsStage")
	
	override fun deserialize(decoder: Decoder): T {
		return create(decoder.decodeInt())
	}
	
	override fun serialize(encoder: Encoder, value: T) {
		encoder.encodeInt(value.value)
	}
}

class SrsStageSerializer : KSerializer<SrsStage> by srsStageSerializer({ SrsStage(it) })

class SrsStageStartedSerializer :
	KSerializer<SrsStage.Started> by srsStageSerializer({
		SrsStage(it) as? SrsStage.Started
			?: throw IllegalArgumentException("Invalid Started SRS stage value: $it (expected 1..8)")
	})

class SrsStageStartedOrBurnedSerializer :
	KSerializer<SrsStage.StartedOrBurned> by srsStageSerializer({
		SrsStage(it) as? SrsStage.StartedOrBurned
			?: throw IllegalArgumentException("Invalid Started SRS stage value: $it (expected 1..9)")
	})