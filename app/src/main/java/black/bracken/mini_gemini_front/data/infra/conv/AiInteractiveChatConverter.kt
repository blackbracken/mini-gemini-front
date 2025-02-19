package black.bracken.mini_gemini_front.data.infra.conv

import black.bracken.mini_gemini_front.data.infra.room.entity.InteractiveHistory
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChat
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChatSpeaker

object AiInteractiveChatConverter {

  fun InteractiveHistory.toDomain(): AiInteractiveChat {
    return AiInteractiveChat(
      id = id,
      speaker = AiInteractiveChatSpeaker.fromGeminiValue(speaker),
      content = content,
    )
  }

  fun AiInteractiveChat.toEntity(): InteractiveHistory {
    return InteractiveHistory(
      id = id,
      speaker = speaker.geminiValue,
      content = content,
    )
  }

}