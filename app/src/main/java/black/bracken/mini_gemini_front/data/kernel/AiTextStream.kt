package black.bracken.mini_gemini_front.data.kernel

data class AiTextStream(
    val parts: List<AiTextStreamPart>,
) {
    val joinedText by lazy {
        parts.joinToString(separator = "") { part ->
            when (part) {
                is AiTextStreamPart.Success -> part.text
                AiTextStreamPart.Error -> ""
            }
        }
    }

    companion object {
        val Initial = AiTextStream(emptyList())
    }
}

sealed interface AiTextStreamPart {
    data class Success(val text: String) : AiTextStreamPart
    data object Error : AiTextStreamPart
}