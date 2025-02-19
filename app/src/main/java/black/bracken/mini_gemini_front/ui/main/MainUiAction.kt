package black.bracken.mini_gemini_front.ui.main

data class MainUiAction(
  val onChangeTab: (MainSelectedTab) -> Unit,
  val onChangeTextStreamQueryText: (String) -> Unit,
  val onClickTextStreamQuerySendButton: () -> Unit,
  val onChangeInteractiveText: (String) -> Unit,
  val onClickInteractiveQuerySendButton: () -> Unit,
  val onClickInteractiveDeleteAllButton: () -> Unit,
) {
  companion object {
    val Noop = MainUiAction(
      onChangeTab = {},
      onChangeTextStreamQueryText = {},
      onClickTextStreamQuerySendButton = {},
      onChangeInteractiveText = {},
      onClickInteractiveQuerySendButton = {},
      onClickInteractiveDeleteAllButton = {},
    )
  }
}