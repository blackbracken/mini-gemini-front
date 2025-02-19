package black.bracken.mini_gemini_front.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart
import black.bracken.mini_gemini_front.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository,
) : ViewModel() {

    private val selectedTab = mutableStateOf(MainSelectedTab.TextStream)
    private val textStreamQuery = mutableStateOf("何かの物語を書いて")
    private val textStreamAnswerPartsFlow = mutableStateOf(flowOf(emptyList<AiTextStreamPart>()))

    val uiState = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
        toUiState()
    }

    fun onClickTab(tab: MainSelectedTab) {
        selectedTab.value = tab
    }

    fun onChangeTextStreamQueryText(text: String) {
        textStreamQuery.value = text
    }

    fun onClickTextStreamQuerySendButton() {
        val query = textStreamQuery.value

        if (query.isNotBlank()) {
            textStreamAnswerPartsFlow.value = geminiRepository.requestTextStream(query)
        }
    }

    @Composable
    private fun toUiState(): MainUiState {
        val selectedTab by selectedTab
        val textStreamQuery by textStreamQuery
        val textStreamAnswerPartsFlow by textStreamAnswerPartsFlow
        val textStreamAnswerParts by textStreamAnswerPartsFlow.collectAsState(emptyList())
        val textStreamAnswer = remember(textStreamAnswerParts) {
            textStreamAnswerParts
                .joinToString("") { part ->
                    when (part) {
                        is AiTextStreamPart.Success -> part.text
                        is AiTextStreamPart.Error -> "" // skip
                    }
                }
                .takeIf { it.isNotBlank() }
        }

        return MainUiState(
            selectedTab = selectedTab,
            textStreamQuery = textStreamQuery,
            textStreamAnswer = textStreamAnswer,
        )
    }

}