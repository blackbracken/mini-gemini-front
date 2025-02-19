package black.bracken.mini_gemini_front.repository

import black.bracken.mini_gemini_front.BuildConfig
import black.bracken.mini_gemini_front.data.infra.GeminiInteractiveResponse
import black.bracken.mini_gemini_front.data.infra.conv.AiInteractiveChatConverter.toDomain
import black.bracken.mini_gemini_front.data.infra.conv.AiInteractiveChatConverter.toEntity
import black.bracken.mini_gemini_front.data.infra.conv.GeminiEntityConverter
import black.bracken.mini_gemini_front.data.infra.room.dao.InteractiveHistoryDao
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChat
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChatSpeaker
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart
import black.bracken.mini_gemini_front.util.mgfJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GeminiRepository {
  fun requestTextStream(query: String): Flow<List<AiTextStreamPart>>
  fun getInteractiveHistory(): Flow<List<AiInteractiveChat>>
  suspend fun requestInteractive(query: String): Result<Unit>
  suspend fun deleteAllInteractiveHistory()
}

class GeminiRepositoryImpl @Inject constructor(
  private val httpClient: HttpClient,
  private val interactiveHistoryDao: InteractiveHistoryDao,
) : GeminiRepository {

  override fun requestTextStream(query: String): Flow<List<AiTextStreamPart>> {
    return flow {
      httpClient
        .preparePost(URL_GEMINI_1_5_FLASH_STREAM_GEN) {
          contentType(ContentType.Application.Json)
          setBody(
            mgfJson.encodeToString(GeminiEntityConverter.createStreamRequest(query))
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
      .map {
        val resp = GeminiEntityConverter.createStreamResponse(it)

        when (resp) {
          null -> AiTextStreamPart.Error
          else -> AiTextStreamPart.Success(resp.extractText())
        }
      }
      .runningFold(emptyList()) { acc, value -> acc + value }
  }

  override fun getInteractiveHistory(): Flow<List<AiInteractiveChat>> {
    return interactiveHistoryDao.getAllFlow()
      .flowOn(Dispatchers.IO)
      .map { list ->
        list.map { it.toDomain() }
      }
  }

  override suspend fun requestInteractive(query: String): Result<Unit> {
    return runCatching {
      withContext(Dispatchers.IO) {
        interactiveHistoryDao.insert(
          AiInteractiveChat(
            speaker = AiInteractiveChatSpeaker.User,
            content = query,
          ).toEntity()
        )

        val history = interactiveHistoryDao.getAll().map { it.toDomain() }

        val resp = httpClient
          .post(URL_GEMINI_1_5_FLASH_GEN) {
            contentType(ContentType.Application.Json)
            setBody(
              mgfJson.encodeToString(
                GeminiEntityConverter.createInteractiveRequest(history, query)
              )
            )
          }
          .body<GeminiInteractiveResponse>()

        interactiveHistoryDao.insert(
          AiInteractiveChat(
            speaker = AiInteractiveChatSpeaker.Ai,
            content = resp.extractText(),
          ).toEntity()
        )
      }
    }
  }

  override suspend fun deleteAllInteractiveHistory() {
    interactiveHistoryDao.deleteAll()
  }

  companion object {
    private const val URL_GEMINI_1_5_FLASH_STREAM_GEN =
      "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent?alt=sse&key=${BuildConfig.API_KEY}"

    private const val URL_GEMINI_1_5_FLASH_GEN =
      "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${BuildConfig.API_KEY}"
  }

}

