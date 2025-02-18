package black.bracken.mini_gemini_front.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import black.bracken.mini_gemini_front.data.kernel.AiTextStream
import black.bracken.mini_gemini_front.ui.theme.MiniGeminiFrontTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val x = viewModel.text.collectAsState(initial = AiTextStream.Initial)
            MiniGeminiFrontTheme {
                MainScreen(
                    uiState = MainUiState(
                        selectedTab = MainSelectedTab.TextStream,
                        textStreamAnswer = x.value,
                        textStreamQuery = "Write a story about a magic backpack.",
                    ),
                    uiAction = MainUiAction(
                        onClickTab = { tab -> viewModel },
                        onClickTextStreamQuerySendButton = { viewModel },
                        onChangeTextStreamQueryText = { viewModel }
                    )
                )
            }
        }
    }
}

