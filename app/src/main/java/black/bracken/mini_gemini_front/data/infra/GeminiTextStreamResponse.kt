package black.bracken.mini_gemini_front.data.infra

import kotlinx.serialization.Serializable

@Serializable
data class GeminiTextStreamResponse(
  val data: GeminiTextStreamResponseData,
) {

  fun extractText(): String {
    return data.candidates.first().content.parts.first().text
  }

}

@Serializable
data class GeminiTextStreamResponseData(
  val candidates: List<GeminiTextStreamResponseCandidate>,
  val usageMetadata: GeminiTextStreamResponseUsageMetadata,
  val modelVersion: String,
)

@Serializable
data class GeminiTextStreamResponseUsageMetadata(
  val promptTokenCount: Int,
  val totalTokenCount: Int,
)

@Serializable
data class GeminiTextStreamResponseCandidate(
  val content: GeminiTextStreamResponseCandidateContent
)

@Serializable
data class GeminiTextStreamResponseCandidateContent(
  val parts: List<GeminiTextStreamResponseCandidatePart>,
  val role: String,
  val finishReason: String? = null,
)

@Serializable
data class GeminiTextStreamResponseCandidatePart(
  val text: String,
)