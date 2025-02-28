package com.example.flappybirdbycursor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flappybirdbycursor.model.GameScreen
import com.example.flappybirdbycursor.viewmodel.GameViewModel

@Composable
fun WelcomeScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Flappy Bird",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = { viewModel.startGame() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "开始游戏",
                fontSize = 24.sp
            )
        }
        
        Button(
            onClick = { viewModel.navigateToScreen(GameScreen.HISTORY) }
        ) {
            Text(
                text = "历史记录",
                fontSize = 24.sp
            )
        }
    }
} 