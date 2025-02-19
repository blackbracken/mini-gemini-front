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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val geminiRepository: GeminiRepository,
) : ViewModel() {

  private val selectedTab = mutableStateOf(MainSelectedTab.TextStream)
  private val textStreamQuery = mutableStateOf("AIについて長文で論考して")
  private val textStreamAnswerPartsFlow = mutableStateOf(flowOf(emptyList<AiTextStreamPart>()))
  private val interactiveQuery = mutableStateOf("気分はどう?")
  private val interactiveHistory = geminiRepository.getInteractiveHistory().stateIn(
    viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = emptyList()
  )

  val uiState = viewModelScope.launchMolecule(RecompositionMode.Immediate) {
    toUiState()
  }

  fun onChangeTab(tab: MainSelectedTab) {
    selectedTab.value = tab
  }

  fun onChangeTextStreamQueryText(text: String) {
    textStreamQuery.value = text
  }

  fun onClickTextStreamQuerySendButton() {
    val query = textStreamQuery.value

    if (query.isNotBlank()) {
      textStreamQuery.value = ""
      textStreamAnswerPartsFlow.value = geminiRepository.requestTextStream(query)
    }
  }

  fun onChangeInteractiveQueryText(text: String) {
    interactiveQuery.value = text
  }

  fun onClickInteractiveQuerySendButton() {
    val query = interactiveQuery.value

    if (query.isNotBlank()) {
      interactiveQuery.value = ""
      viewModelScope.launch {
        geminiRepository.requestInteractive(query)
      }
    }
  }

  fun onClickInteractiveDeleteAllButton() {
    viewModelScope.launch {
      geminiRepository.deleteAllInteractiveHistory()
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
    val textInteractiveQuery by interactiveQuery
    val interactiveHistory by interactiveHistory.collectAsState()

    return MainUiState(
      selectedTab = selectedTab,
      textStreamQuery = textStreamQuery,
      textStreamAnswer = textStreamAnswer,
      interactiveQuery = textInteractiveQuery,
      interactiveHistory = interactiveHistory,
    )
  }

}