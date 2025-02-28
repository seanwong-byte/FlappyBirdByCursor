package com.example.flappybirdbycursor.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flappybirdbycursor.model.Bird
import com.example.flappybirdbycursor.model.GameConfig
import com.example.flappybirdbycursor.model.GameScreen
import com.example.flappybirdbycursor.model.Pipe
import com.example.flappybirdbycursor.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState = viewModel.gameState
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // 约60FPS
            viewModel.updateGame()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
            .clickable { viewModel.jump() }
    ) {
        // 绘制游戏元素
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // 绘制管道
            gameState.pipes.forEach { pipe ->
                drawPipe(pipe)
            }
            
            // 绘制小鸟
            drawBird(gameState.bird)
            
            // 绘制地面
            drawGround()
        }
        
        // 显示分数
        Text(
            text = "${gameState.score}",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )
        
        // 游戏结束界面
        if (gameState.isGameOver) {
            GameOverOverlay(
                score = gameState.score,
                onRestart = { viewModel.startGame() },
                onBack = { viewModel.navigateToScreen(GameScreen.WELCOME) }
            )
        }
    }
}

@Composable
private fun GameOverOverlay(
    score: Int,
    onRestart: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "游戏结束",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "得分: $score",
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onRestart) {
                    Text(
                        text = "重新开始",
                        fontSize = 24.sp
                    )
                }
                
                Button(onClick = onBack) {
                    Text(
                        text = "返回主菜单",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBird(bird: Bird) {
    rotate(
        degrees = bird.rotation,
        pivot = Offset(bird.x, bird.y)
    ) {
        // 绘制鸟的身体（椭圆形）
        drawOval(
            color = Color.Yellow,
            topLeft = Offset(
                bird.x - GameConfig.BIRD_WIDTH / 2,
                bird.y - GameConfig.BIRD_HEIGHT / 2
            ),
            size = Size(GameConfig.BIRD_WIDTH, GameConfig.BIRD_HEIGHT)
        )
        
        // 绘制鸟的眼睛（小圆点）
        drawCircle(
            color = Color.Black,
            radius = 5f,
            center = Offset(
                bird.x + GameConfig.BIRD_WIDTH / 4,
                bird.y - GameConfig.BIRD_HEIGHT / 6
            )
        )
        
        // 绘制鸟的嘴（三角形）
        val trianglePath = Path().apply {
            moveTo(
                bird.x + GameConfig.BIRD_WIDTH / 2,
                bird.y
            )
            lineTo(
                bird.x + GameConfig.BIRD_WIDTH / 2 + 15f,
                bird.y - 5f
            )
            lineTo(
                bird.x + GameConfig.BIRD_WIDTH / 2 + 15f,
                bird.y + 5f
            )
            close()
        }
        drawPath(
            path = trianglePath,
            color = Color.Red
        )
        
        // 绘制鸟的翅膀（椭圆形）
        drawOval(
            color = Color(0xFFFFD700),
            topLeft = Offset(
                bird.x - GameConfig.BIRD_WIDTH / 4,
                bird.y - GameConfig.BIRD_HEIGHT / 2
            ),
            size = Size(GameConfig.BIRD_WIDTH / 2, GameConfig.BIRD_HEIGHT / 3)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPipe(pipe: Pipe) {
    val pipeColor = Color(0xFF2E8B57) // 深绿色
    val pipeHighlightColor = Color(0xFF3CB371) // 中等海洋绿
    
    // 上管道
    drawRect(
        color = pipeColor,
        topLeft = Offset(pipe.x, 0f),
        size = Size(pipe.width, pipe.topHeight)
    )
    
    // 上管道的高亮边缘
    drawRect(
        color = pipeHighlightColor,
        topLeft = Offset(pipe.x + pipe.width - 10f, 0f),
        size = Size(10f, pipe.topHeight)
    )
    
    // 上管道的底部装饰
    drawRect(
        color = pipeHighlightColor,
        topLeft = Offset(pipe.x - 10f, pipe.topHeight - 20f),
        size = Size(pipe.width + 20f, 20f)
    )
    
    // 下管道
    drawRect(
        color = pipeColor,
        topLeft = Offset(
            pipe.x,
            pipe.topHeight + pipe.gap
        ),
        size = Size(
            pipe.width,
            size.height - (pipe.topHeight + pipe.gap)
        )
    )
    
    // 下管道的高亮边缘
    drawRect(
        color = pipeHighlightColor,
        topLeft = Offset(
            pipe.x + pipe.width - 10f,
            pipe.topHeight + pipe.gap
        ),
        size = Size(
            10f,
            size.height - (pipe.topHeight + pipe.gap)
        )
    )
    
    // 下管道的顶部装饰
    drawRect(
        color = pipeHighlightColor,
        topLeft = Offset(
            pipe.x - 10f,
            pipe.topHeight + pipe.gap
        ),
        size = Size(pipe.width + 20f, 20f)
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGround() {
    drawRect(
        color = Color(0xFF964B00),
        topLeft = Offset(0f, size.height - 100f),
        size = Size(size.width, 100f)
    )
} 