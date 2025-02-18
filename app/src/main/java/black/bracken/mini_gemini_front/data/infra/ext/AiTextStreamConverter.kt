package black.bracken.mini_gemini_front.data.infra.ext

import black.bracken.mini_gemini_front.data.infra.GeminiTextStreamResponse
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart

object AiTextStreamConverter {

    fun fromGeminiResponse(response: GeminiTextStreamResponse?): AiTextStreamPart {
        return when (response) {
            null -> AiTextStreamPart.Error
            else -> AiTextStreamPart.Success(response.data.candidates.first().content.parts.first().text)
        }
    }

}