package black.bracken.mini_gemini_front.data.infra.ext

import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequest
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequestContent
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamRequestPart
import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamResponse
import black.bracken.mini_gemini_front.util.mgfJson

// TODO: rename
object GeminiConverter {

    fun toRequest(requestQuery: String): GeminiTextStreamRequest {
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

    fun toResponse(rawResponse: String): GeminiTextStreamResponse? {
        return try {
            mgfJson.decodeFromString("{${rawResponse}}")
        } catch (e: Exception) {
            null
        }
    }

}