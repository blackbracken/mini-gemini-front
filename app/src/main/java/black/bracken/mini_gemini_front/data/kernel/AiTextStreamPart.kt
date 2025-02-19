package black.bracken.mini_gemini_front.data.kernel

sealed interface AiTextStreamPart {
  data class Success(val text: String) : AiTextStreamPart
  data object Error : AiTextStreamPart
}