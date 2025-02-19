package black.bracken.mini_gemini_front.data.infra

import kotlinx.serialization.Serializable

// example for copilot:
// {
//    "contents": [
//      {"role":"user",
//       "parts":[{
//         "text": "Hello"}]},
//      {"role": "model",
//       "parts":[{
//         "text": "Great to meet you. What would you like to know?"}]},
//      {"role":"user",
//       "parts":[{
//         "text": "I have two dogs in my house. How many paws are in my house?"}]},
//    ]
// }

@Serializable
data class GeminiInteractiveRequest(
  val contents: List<GeminiInteractiveRequestContent>,
)

@Serializable
data class GeminiInteractiveRequestContent(
  val role: String,
  val parts: List<GeminiInteractiveRequestPart>,
)

@Serializable
data class GeminiInteractiveRequestPart(
  val text: String,
)