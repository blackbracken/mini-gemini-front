package black.bracken.mini_gemini_front.data.infra

import kotlinx.serialization.Serializable

@Serializable
data class GeminiTextStreamRequest(
  val contents: List<GeminiTextStreamRequestContent>,
)

@Serializable
data class GeminiTextStreamRequestContent(
  val parts: List<GeminiTextStreamRequestPart>,
)

@Serializable
data class GeminiTextStreamRequestPart(
  val text: String,
)
