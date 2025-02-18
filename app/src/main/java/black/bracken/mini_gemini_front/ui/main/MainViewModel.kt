package black.bracken.mini_gemini_front.ui.main

import androidx.lifecycle.ViewModel
import black.bracken.mini_gemini_front.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository,
) : ViewModel() {

    val text = geminiRepository.requestTextStream("Write a story about a magic backpack.")

}