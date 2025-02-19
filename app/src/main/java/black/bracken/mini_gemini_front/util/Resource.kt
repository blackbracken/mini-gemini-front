package black.bracken.mini_gemini_front.util

sealed interface Resource<out T> {
    data class Success<T>(val value: T) : Resource<T>
    data object Loading : Resource<Nothing>
    data class Error(val exception: Exception) : Resource<Nothing>
}