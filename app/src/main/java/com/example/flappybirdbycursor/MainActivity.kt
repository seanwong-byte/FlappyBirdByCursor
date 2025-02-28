package com.example.flappybirdbycursor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.flappybirdbycursor.model.GameScreen
import com.example.flappybirdbycursor.ui.screens.GameScreen
import com.example.flappybirdbycursor.ui.screens.HistoryScreen
import com.example.flappybirdbycursor.ui.screens.WelcomeScreen
import com.example.flappybirdbycursor.ui.theme.FlappyBirdByCursorTheme
import com.example.flappybirdbycursor.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlappyBirdByCursorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (viewModel.currentScreen) {
                        GameScreen.WELCOME -> WelcomeScreen(viewModel)
                        GameScreen.GAME -> GameScreen(viewModel)
                        GameScreen.HISTORY -> HistoryScreen(viewModel)
                    }
                }
            }
        }
    }
}