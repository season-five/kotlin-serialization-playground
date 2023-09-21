import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

object CFPRSerializer : KSerializer<ClosedFloatingPointRange<Double>> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("double-range") {
            element<Double>("start")
            element<Double>("endEnclusive")
        }

    override fun serialize(encoder: Encoder, value: ClosedFloatingPointRange<Double>) {
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.start)
            encodeDoubleElement(descriptor, 1, value.endInclusive)
        }
    }

    override fun deserialize(decoder: Decoder): ClosedFloatingPointRange<Double> {
        var start: Double? = null
        var end: Double? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                val index = decodeElementIndex(descriptor)
                if (index == CompositeDecoder.DECODE_DONE) break
                if (index == 0) start = decodeDoubleElement(descriptor, index)
                else end = decodeDoubleElement(descriptor, index)
            }
            if (start == null || end == null) throw SerializationException("...")

        }
        return start!!..end!!
    }
}

val module = SerializersModule {
    polymorphicDefaultSerializer(Any::class) {
        @Suppress("UNCHECKED_CAST") when (it) {
            is ClosedFloatingPointRange<*> -> CFPRSerializer as SerializationStrategy<Any>
            else -> null
        }
    }
    polymorphicDefaultDeserializer(Any::class) {
        when (it) {
            "double-range" -> CFPRSerializer
            else -> null
        }
    }
}

private val json = Json { serializersModule = module }

fun main() {
    val s = json.encodeToString(mapOf<String, @Polymorphic Any>("test" to 1.0..3.0))
    println(s) // Project(name=kotlinx.serialization, language=Kotlin)
    val o = json.decodeFromString<Map<String, @Polymorphic Any>>(s)
    println(o)
}

