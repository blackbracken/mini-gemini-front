package black.bracken.mini_gemini_front.ui.main

import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChat
import black.bracken.mini_gemini_front.ui.main.MainSelectedTab.entries

data class MainUiState(
  val selectedTab: MainSelectedTab,
  val textStreamQuery: String,
  val textStreamAnswer: String?,
  val interactiveQuery: String,
  val interactiveHistory: List<AiInteractiveChat>,
) {
  companion object {
    val Dummy = MainUiState(
      selectedTab = MainSelectedTab.TextStream,
      textStreamAnswer = null,
      textStreamQuery = "",
      interactiveQuery = "",
      interactiveHistory = emptyList(),
    )
  }
}

enum class MainSelectedTab(val index: Int, val title: String) {
  TextStream(0, "テキストストリーム"),
  Interactive(1, "インタラクティブ"),
  ;

  companion object {
    fun fromIndex(index: Int): MainSelectedTab = entries.first { it.index == index }
  }
}