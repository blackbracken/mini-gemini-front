@file:OptIn(ExperimentalMaterial3Api::class)

package black.bracken.mini_gemini_front.ui.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import black.bracken.mini_gemini_front.data.kernel.AiTextStream
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart
import black.bracken.mini_gemini_front.ui.theme.MiniGeminiFrontTheme

data class MainUiState(
    val selectedTab: MainSelectedTab,
    val textStreamQuery: String,
    val textStreamAnswer: AiTextStream,
) {
    companion object {
        val Initial = MainUiState(
            selectedTab = MainSelectedTab.TextStream,
            textStreamAnswer = AiTextStream.Initial,
            textStreamQuery = "",
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

data class MainUiAction(
    val onClickTab: (MainSelectedTab) -> Unit,
    val onClickTextStreamQuerySendButton: () -> Unit,
    val onChangeTextStreamQueryText: () -> Unit,
) {
    companion object {
        val Noop = MainUiAction(
            onClickTab = {},
            onClickTextStreamQuerySendButton = {},
            onChangeTextStreamQueryText = {},
        )
    }
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    uiAction: MainUiAction,
) {
    val pagerState = rememberPagerState { MainSelectedTab.entries.size }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "MiniGeminiFront") },
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
                        onClick = { uiAction.onClickTab(tab) },
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
                    MainSelectedTab.TextStream -> MainTextStreamContent()
                    MainSelectedTab.Interactive -> MainInteractiveContent()
                }
            }
        }
    }

}

@Composable
private fun MainTextStreamContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                TextStreamAnswer(
                    answer = AiTextStream(
                        parts = listOf(
                            AiTextStreamPart.Success("Hello, world!"),
                            AiTextStreamPart.Success("こんにちは、世界！"),
                        )
                    ),
                )
            }
        }
        TextStreamInput(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TextStreamAnswer(
    answer: AiTextStream,
) {
    val text = answer.joinedText
    if (text.isBlank()) {
        return
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
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
            )
        }
    }

}

@Composable
private fun MainInteractiveContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

    }
}

@Composable
private fun TextStreamInput(
    modifier: Modifier,
) {
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
                value = "hello",
                onValueChange = {},
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {},
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
            uiState = MainUiState.Initial,
            uiAction = MainUiAction.Noop,
        )
    }
}