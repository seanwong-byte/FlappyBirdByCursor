package com.example.flappybirdbycursor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flappybirdbycursor.model.GameScreen
import com.example.flappybirdbycursor.model.ScoreRecord
import com.example.flappybirdbycursor.viewmodel.GameViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val scores by viewModel.topScores.collectAsState(initial = emptyList())
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "历史最高分",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(scores) { score ->
                ScoreItem(score = score)
            }
        }
        
        Button(
            onClick = { viewModel.navigateToScreen(GameScreen.WELCOME) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "返回",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun ScoreItem(
    score: ScoreRecord,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${score.score}分",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = dateFormat.format(score.date),
            fontSize = 16.sp
        )
    }
} 