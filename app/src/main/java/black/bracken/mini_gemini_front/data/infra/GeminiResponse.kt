package black.bracken.mini_gemini_front.data.infra

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val data: GeminiResponseData,
)

@Serializable
data class GeminiResponseData(
    val candidates: List<GeminiResponseCandidate>,
    val usageMetadata: GeminiResponseUsageMetadata,
    val modelVersion: String,
)

@Serializable
data class GeminiResponseUsageMetadata(
    val promptTokenCount: Int,
    val totalTokenCount: Int,
)

@Serializable
data class GeminiResponseCandidate(
    val content: GeminiResponseCandidateContent
)

@Serializable
data class GeminiResponseCandidateContent(
    val parts: List<GeminiResponseCandidatePart>,
    val role: String,
    val finishReason: String? = null,
)

@Serializable
data class GeminiResponseCandidatePart(
    val text: String,
)