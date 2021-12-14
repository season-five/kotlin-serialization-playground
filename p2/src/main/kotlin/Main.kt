import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class SocketResponse(val channel: String,
    val minimumVersion: String,
    val clientId: String,
    val id: String,
    val version: String,
    val successful: Boolean,
)

val BE_RESPONSE = "[{\"ext\":{\"CamelHeaders\":{\"correlationId\":\"babd5db9-5c34-11ec-83be-87e0cb6841d2\"}},\"minimumVersion\":\"1.0\",\"clientId\":\"2i17fx00loyg2hfkmw33sz6udr\",\"supportedConnectionTypes\":[\"websocket\"],\"advice\":{\"interval\":5000,\"timeout\":0,\"reconnect\":\"retry\"},\"channel\":\"/meta/handshake\",\"id\":\"1\",\"version\":\"1.0\",\"successful\":true}]";

fun main() {
    val obj = Json{ ignoreUnknownKeys = true }.decodeFromString<List<SocketResponse>>(ListSerializer(SocketResponse.serializer()), BE_RESPONSE)
    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)
}

