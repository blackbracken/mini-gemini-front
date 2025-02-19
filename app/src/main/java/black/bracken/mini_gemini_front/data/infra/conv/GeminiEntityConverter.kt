package black.bracken.mini_gemini_front.data.infra.conv

import black.bracken.mini_gemini_front.data.infra.GeminiInteractiveRequest
import black.bracken.mini_gemini_front.data.infra.GeminiInteractiveRequestContent
import black.bracken.mini_gemini_front.data.infra.GeminiInteractiveRequestPart
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequest
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequestContent
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequestPart
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamResponse
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChat
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChatSpeaker
import black.bracken.mini_gemini_front.util.mgfJson

object GeminiEntityConverter {

  fun createStreamRequest(requestQuery: String): GeminiTextStreamRequest {
    return GeminiTextStreamRequest(
      contents = listOf(
        GeminiTextStreamRequestContent(
          parts = listOf(
            GeminiTextStreamRequestPart(
              text = requestQuery
            )
          )
        )
      )
    )
  }

  fun createStreamResponse(rawResponse: String): GeminiTextStreamResponse? {
    return try {
      mgfJson.decodeFromString("{${rawResponse}}")
    } catch (_: Exception) {
      null
    }
  }

  fun createInteractiveRequest(
    history: List<AiInteractiveChat>,
    query: String,
  ): GeminiInteractiveRequest {
    return GeminiInteractiveRequest(
      contents = history.map { chat ->
        GeminiInteractiveRequestContent(
          parts = listOf(
            GeminiInteractiveRequestPart(
              text = chat.content
            )
          ),
          role = chat.speaker.geminiValue,
        )
      } + GeminiInteractiveRequestContent(
        parts = listOf(
          GeminiInteractiveRequestPart(
            text = query
          )
        ),
        role = AiInteractiveChatSpeaker.User.geminiValue,
      )
    )
  }

}