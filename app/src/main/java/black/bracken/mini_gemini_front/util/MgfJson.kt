package black.bracken.mini_gemini_front.util

import kotlinx.serialization.json.Json

val mgfJson = Json {
    // cf. https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/-json-builder/
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}