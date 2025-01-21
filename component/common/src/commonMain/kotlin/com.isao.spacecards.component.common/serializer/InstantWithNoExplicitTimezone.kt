package com.isao.spacecards.component.common.serializer

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [Instant] serializer that serializes and deserializes to and from a string with no timezone.
 * Astrobin API timestamps are in UTC, but with no explicit timezone.
 * This is a workaround for kotlinx.serialization not supporting Instant with no explicit timezone.
 */
object InstantWithNoExplicitTimezone : KSerializer<Instant> {
  override val descriptor =
    PrimitiveSerialDescriptor("kotlinx.datetime.Instant", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: Instant,
  ) = encoder.encodeString(value.toString().dropLast(1))

  override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString() + "z")
}
