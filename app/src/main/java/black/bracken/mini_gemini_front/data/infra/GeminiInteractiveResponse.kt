package black.bracken.mini_gemini_front.data.infra

import kotlinx.serialization.Serializable

// example for copilot:
// {
//   "candidates": [
//     {
//       "content": {
//         "parts": [
//           {
//             "text": "Each dog has 4 paws, and you have 2 dogs, so there are 2 * 4 = 8 paws in your house.\n"
//           }
//         ],
//         "role": "model"
//       },
//       "finishReason": "STOP",
//       "avgLogprobs": -0.0096212500526059053
//     }
//   ],
//   "usageMetadata": {
//     "promptTokenCount": 29,
//     "candidatesTokenCount": 31,
//     "totalTokenCount": 60,
//     "promptTokensDetails": [
//       {
//         "modality": "TEXT",
//         "tokenCount": 29
//       }
//     ],
//     "candidatesTokensDetails": [
//       {
//         "modality": "TEXT",
//         "tokenCount": 31
//       }
//     ]
//   },
//   "modelVersion": "gemini-1.5-flash"
// }

@Serializable
data class GeminiInteractiveResponse(
  val candidates: List<GeminiInteractiveResponseCandidates>,
  val usageMetadata: GeminiInteractiveResponseUsageMetadata,
  val modelVersion: String,
) {

  fun extractText(): String {
    return candidates.first().content.parts.first().text
  }

}

@Serializable
data class GeminiInteractiveResponseUsageMetadata(
  val promptTokenCount: Int,
  val totalTokenCount: Int,
)

@Serializable
data class GeminiInteractiveResponseCandidates(
  val content: GeminiInteractiveResponseCandidateContent,
  val finishReason: String? = null,
)

@Serializable
data class GeminiInteractiveResponseCandidateContent(
  val parts: List<GeminiInteractiveResponseCandidatePart>,
  val role: String,
)

@Serializable
data class GeminiInteractiveResponseCandidatePart(
  val text: String,
)