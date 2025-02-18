package black.bracken.mini_gemini_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import black.bracken.mini_gemini_front.data.kernel.AiTextStream
import black.bracken.mini_gemini_front.data.kernel.AiTextStreamPart
import black.bracken.mini_gemini_front.ui.main.MainViewModel
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(
                            text = x.value.parts.joinToString("") {
                                when (it) {
                                    is AiTextStreamPart.Success -> it.text
                                    is AiTextStreamPart.Error -> ""
                                }
                            },
                            color = Color.Black,
                            fontSize = 8.sp,
                        )
                    }
                }
            }
        }
    }
}

