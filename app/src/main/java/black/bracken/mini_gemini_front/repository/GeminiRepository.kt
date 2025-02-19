package black.bracken.mini_gemini_front.repository

import black.bracken.mini_gemini_front.BuildConfig
import black.bracken.mini_gemini_front.data.infra.ext.AiTextStreamPartConverter
import black.bracken.mini_gemini_front.data.infra.ext.GeminiConverter
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart
import black.bracken.mini_gemini_front.util.mgfJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import javax.inject.Inject

interface GeminiRepository {
    fun requestTextStream(query: String): Flow<List<AiTextStreamPart>>
}

class GeminiRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : GeminiRepository {

    override fun requestTextStream(query: String): Flow<List<AiTextStreamPart>> {
        return flow {
            httpClient
                .preparePost(URL_GEMINI_1_5_FLASH) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        mgfJson.encodeToString(GeminiConverter.toRequest(query))
                    )
                }
                .execute { resp ->
                    val channel: ByteReadChannel = resp.body()
                    while (!channel.isClosedForRead) {
                        emit(channel.readUTF8Line() ?: break)
                    }
                }
        }
            .flowOn(Dispatchers.IO)
            .filter { it.isNotBlank() }
            .map { AiTextStreamPartConverter.fromGeminiResponse(GeminiConverter.toResponse(it)) }
            .runningFold(emptyList()) { acc, value -> acc + value }
    }

    companion object {
        private const val URL_GEMINI_1_5_FLASH =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent?alt=sse&key=${BuildConfig.API_KEY}"
    }

}

