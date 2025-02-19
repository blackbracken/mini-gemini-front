package black.bracken.mini_gemini_front.data.kernel

data class AiInteractiveChat(
  val id: Int = 0,
  val speaker: AiInteractiveChatSpeaker,
  val content: String,
)

enum class AiInteractiveChatSpeaker(val geminiValue: String) {
  User("user"),
  Ai("model"),
  ;

  companion object {
    fun fromGeminiValue(value: String): AiInteractiveChatSpeaker {
      return when (value) {
        "user" -> User
        "model" -> Ai
        else -> throw IllegalStateException("unreachable")
      }
    }
  }

}