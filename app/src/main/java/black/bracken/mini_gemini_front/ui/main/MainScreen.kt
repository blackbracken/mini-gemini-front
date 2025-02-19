@file:OptIn(ExperimentalMaterial3Api::class)

package black.bracken.mini_gemini_front.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChat
import black.bracken.mini_gemini_front.data.kernel.AiInteractiveChatSpeaker
import black.bracken.mini_gemini_front.ui.theme.MiniGeminiFrontTheme

@Composable
fun MainScreen(
  uiState: MainUiState,
  uiAction: MainUiAction,
) {
  val pagerState = rememberPagerState { MainSelectedTab.entries.size }
  LaunchedEffect(uiState.selectedTab) {
    pagerState.animateScrollToPage(uiState.selectedTab.index)
  }
  LaunchedEffect(pagerState.currentPage) {
    if (pagerState.currentPage != uiState.selectedTab.index) {
      uiAction.onChangeTab(MainSelectedTab.fromIndex(pagerState.currentPage))
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "MiniGeminiFront") },
        actions = {
          AnimatedVisibility(
            visible = uiState.selectedTab == MainSelectedTab.Interactive,
            enter = fadeIn(),
            exit = fadeOut(),
          ) {
            IconButton(onClick = uiAction.onClickInteractiveDeleteAllButton) {
              Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
              )
            }
          }
        }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      TabRow(
        selectedTabIndex = uiState.selectedTab.index,
      ) {
        MainSelectedTab.entries.forEach { tab ->
          Tab(
            selected = uiState.selectedTab == tab,
            onClick = { uiAction.onChangeTab(tab) },
            modifier = Modifier
              .defaultMinSize(minHeight = 64.dp)
              .background(MaterialTheme.colorScheme.surfaceContainer)
          ) {
            Text(text = tab.title)
          }
        }
      }
      HorizontalPager(
        state = pagerState,
      ) { pageIndex ->
        when (MainSelectedTab.fromIndex(pageIndex)) {
          MainSelectedTab.TextStream -> MainTextStreamContent(
            uiState = uiState,
            uiAction = uiAction,
          )

          MainSelectedTab.Interactive -> MainInteractiveContent(
            uiState = uiState,
            uiAction = uiAction,
          )
        }
      }
    }
  }

}

@Composable
private fun MainTextStreamContent(
  uiState: MainUiState,
  uiAction: MainUiAction,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
    if (uiState.textStreamAnswer.isNullOrBlank()) {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .alpha(0.66f),
      ) {
        Icon(
          imageVector = Icons.Filled.Person,
          contentDescription = null,
          modifier = Modifier.size(128.dp),
          tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "難解な問題の解き方や、考察をGeminiに聞いてみよう",
          fontSize = 12.sp,
          maxLines = 1,
          textAlign = TextAlign.Center,
        )
      }
    } else {
      Column(
        modifier = Modifier
          .verticalScroll(
            state = rememberScrollState(),
            reverseScrolling = true,
          )
          .weight(1f)
          .padding(
            vertical = 32.dp,
            horizontal = 16.dp,
          ),
      ) {
        TextStreamAnswer(
          text = uiState.textStreamAnswer,
        )
      }
    }
    TextStreamInput(
      input = uiState.textStreamQuery,
      onChangeInput = uiAction.onChangeTextStreamQueryText,
      onClickSendButton = uiAction.onClickTextStreamQuerySendButton,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

@Composable
private fun TextStreamAnswer(
  text: String?,
) {
  if (text.isNullOrBlank()) {
    return
  }

  Row(
    modifier = Modifier.fillMaxWidth()
  ) {
    Icon(
      imageVector = Icons.Filled.Person,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(40.dp)
    )

    Spacer(modifier = Modifier.width(12.dp))

    Box(
      Modifier
        .fillMaxWidth()
        .animateContentSize()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
      Text(
        text = text,
        fontSize = 10.sp,
      )
    }
  }
}

@Composable
private fun InteractiveChat(
  chat: AiInteractiveChat,
) {
  when (chat.speaker) {
    AiInteractiveChatSpeaker.User -> {
      InteractiveChatUser(chat.content)
    }

    AiInteractiveChatSpeaker.Ai -> {
      InteractiveChatAi(chat.content)
    }
  }
}

@Composable
private fun InteractiveChatUser(content: String) {
  Row(
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      Modifier
        .weight(1f)
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
      Text(
        text = content,
        fontSize = 12.sp,
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Icon(
      imageVector = Icons.Filled.Person,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.size(40.dp)
    )
  }
}

@Composable
private fun InteractiveChatAi(content: String) {
  Row(
    modifier = Modifier.fillMaxWidth()
  ) {
    Icon(
      imageVector = Icons.Filled.Person,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(40.dp)
    )

    Spacer(modifier = Modifier.width(12.dp))

    Box(
      Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
      Text(
        text = content,
        fontSize = 12.sp,
      )
    }
  }
}

@Composable
private fun MainInteractiveContent(
  uiState: MainUiState,
  uiAction: MainUiAction,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(uiState.interactiveHistory) {
      val history = uiState.interactiveHistory
      if (history.isNotEmpty()) {
        scrollState.animateScrollToItem(history.size - 1)
      }
    }

    LazyColumn(
      state = scrollState,
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
    ) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
      items(items = uiState.interactiveHistory, key = { it.id }) {
        Column {
          InteractiveChat(chat = it)
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }
    TextStreamInput(
      input = uiState.interactiveQuery,
      onChangeInput = uiAction.onChangeInteractiveText,
      onClickSendButton = uiAction.onClickInteractiveQuerySendButton,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

@Composable
private fun TextStreamInput(
  input: String,
  modifier: Modifier,
  onChangeInput: (String) -> Unit,
  onClickSendButton: () -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current

  Column(
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(MaterialTheme.colorScheme.primary)
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
        .padding(top = 8.dp, bottom = 2.dp)
        .padding(horizontal = 8.dp),
    ) {
      OutlinedTextField(
        value = input,
        onValueChange = onChangeInput,
        modifier = Modifier.weight(1f),
      )
      Spacer(modifier = Modifier.width(8.dp))
      Button(
        onClick = {
          onClickSendButton()
          focusManager.clearFocus(true)
          keyboardController?.hide()
        },
        modifier = Modifier
          .align(Alignment.CenterVertically),
        shape = MaterialTheme.shapes.large,
      ) {
        Text(text = "送信")
      }
    }
  }
}

@Preview
@Composable
private fun TextStreamInputPreview() {
  MiniGeminiFrontTheme {
    MainScreen(
      uiState = MainUiState.Dummy,
      uiAction = MainUiAction.Noop,
    )
  }
}