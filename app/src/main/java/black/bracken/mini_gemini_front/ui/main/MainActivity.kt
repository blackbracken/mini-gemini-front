package black.bracken.mini_gemini_front.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import black.bracken.mini_gemini_front.ui.theme.MiniGeminiFrontTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MiniGeminiFrontTheme {
                MainScreen(
                    uiState = uiState,
                    uiAction = MainUiAction(
                        onClickTab = viewModel::onClickTab,
                        onChangeTextStreamQueryText = viewModel::onChangeTextStreamQueryText,
                        onClickTextStreamQuerySendButton = viewModel::onClickTextStreamQuerySendButton,
                    )
                )
            }
        }
    }
}

