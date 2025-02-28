package com.example.flappybirdbycursor.viewmodel

import android.app.Application
import android.graphics.RectF
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flappybirdbycursor.R
import com.example.flappybirdbycursor.data.ScoreDatabase
import com.example.flappybirdbycursor.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random
import android.media.MediaPlayer

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ScoreDatabase.getDatabase(application)
    private val scoreDao = database.scoreDao()
    private var currentPipeGap = GameConfig.INITIAL_PIPE_GAP
    private var mediaPlayer: MediaPlayer? = null
    
    var gameState by mutableStateOf(
        GameState(
            bird = Bird(
                x = 100f,
                y = 300f,
                velocity = 0f,
                rotation = 0f
            ),
            pipes = emptyList(),
            score = 0,
            isGameOver = false,
            isPlaying = false
        )
    )
        private set
    
    var currentScreen by mutableStateOf(GameScreen.WELCOME)
        private set
    
    val topScores: Flow<List<ScoreRecord>> = scoreDao.getTopScores()
    
    init {
        setupBackgroundMusic()
    }
    
    private fun setupBackgroundMusic() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.background_music)
            if (mediaPlayer == null) {
                println("Warning: MediaPlayer creation failed")
                return
            }
            mediaPlayer?.apply {
                isLooping = true
                setVolume(1.0f, 1.0f)
                try {
                    prepare()
                } catch (e: Exception) {
                    println("Warning: MediaPlayer prepare failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("Warning: Background music setup failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun startGame() {
        currentPipeGap = GameConfig.INITIAL_PIPE_GAP
        gameState = gameState.copy(
            bird = Bird(
                x = 100f,
                y = 300f,
                velocity = 0f,
                rotation = 0f
            ),
            pipes = generateInitialPipes(),
            score = 0,
            isGameOver = false,
            isPlaying = true
        )
        currentScreen = GameScreen.GAME
        try {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    try {
                        player.seekTo(0)
                        player.start()
                        println("Info: Background music started")
                    } catch (e: Exception) {
                        println("Warning: Failed to start background music: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            println("Warning: Background music start failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun jump() {
        if (!gameState.isGameOver && gameState.isPlaying) {
            gameState = gameState.copy(
                bird = gameState.bird.copy(
                    velocity = GameConfig.JUMP_FORCE,
                    rotation = -45f
                )
            )
        }
    }
    
    fun updateGame() {
        if (!gameState.isGameOver && gameState.isPlaying) {
            // 更新小鸟位置
            val newBird = updateBird(gameState.bird)
            
            // 更新管道位置
            val (newPipes, scoreIncrement) = updatePipes(gameState.pipes, newBird)
            
            // 检查碰撞
            val hasCollision = checkCollision(newBird, newPipes)
            
            if (hasCollision) {
                gameOver()
            } else {
                gameState = gameState.copy(
                    bird = newBird,
                    pipes = newPipes,
                    score = gameState.score + scoreIncrement
                )
            }
        }
    }
    
    private fun updateBird(bird: Bird): Bird {
        val newVelocity = bird.velocity + GameConfig.GRAVITY
        val newY = bird.y + newVelocity
        val newRotation = (bird.rotation + 3f).coerceAtMost(90f)
        
        return bird.copy(
            y = newY,
            velocity = newVelocity,
            rotation = newRotation
        )
    }
    
    private fun updatePipes(pipes: List<Pipe>, bird: Bird): Pair<List<Pipe>, Int> {
        var scoreIncrement = 0
        val newPipes = pipes.map { pipe ->
            val newX = pipe.x - GameConfig.PIPE_SPEED
            val newPassed = !pipe.passed && newX + pipe.width < bird.x
            if (newPassed) {
                scoreIncrement++
                // 缩小管道间隙，但不小于最小值
                currentPipeGap = (currentPipeGap * GameConfig.GAP_DECREASE_RATE)
                    .coerceAtLeast(GameConfig.MIN_PIPE_GAP)
            }
            pipe.copy(x = newX, passed = pipe.passed || newPassed)
        }.filter { it.x + it.width > 0f }
        
        if (newPipes.isEmpty() || newPipes.last().x < GameConfig.PIPE_SPACING) {
            return Pair(newPipes + generatePipe(), scoreIncrement)
        }
        
        return Pair(newPipes, scoreIncrement)
    }
    
    private fun checkCollision(bird: Bird, pipes: List<Pipe>): Boolean {
        // 检查是否撞到屏幕上边界
        if (bird.y - GameConfig.BIRD_HEIGHT / 2 <= 0) {
            return true
        }
        
        // 检查是否撞到地面
        if (bird.y >= 2400f - GameConfig.GROUND_HEIGHT) {
            return true
        }
        
        // 检查是否撞到管道
        for (pipe in pipes) {
            val birdRect = RectF(
                bird.x - GameConfig.BIRD_WIDTH / 2 + GameConfig.COLLISION_TOLERANCE,
                bird.y - GameConfig.BIRD_HEIGHT / 2 + GameConfig.COLLISION_TOLERANCE,
                bird.x + GameConfig.BIRD_WIDTH / 2 - GameConfig.COLLISION_TOLERANCE,
                bird.y + GameConfig.BIRD_HEIGHT / 2 - GameConfig.COLLISION_TOLERANCE
            )
            
            val topPipeRect = RectF(
                pipe.x + GameConfig.COLLISION_TOLERANCE,
                0f,
                pipe.x + pipe.width - GameConfig.COLLISION_TOLERANCE,
                pipe.topHeight
            )
            
            val bottomPipeRect = RectF(
                pipe.x + GameConfig.COLLISION_TOLERANCE,
                pipe.topHeight + currentPipeGap,
                pipe.x + pipe.width - GameConfig.COLLISION_TOLERANCE,
                2400f - GameConfig.GROUND_HEIGHT
            )
            
            if (RectF.intersects(birdRect, topPipeRect) || 
                RectF.intersects(birdRect, bottomPipeRect)) {
                return true
            }
        }
        
        return false
    }
    
    private fun gameOver() {
        gameState = gameState.copy(isGameOver = true)
        mediaPlayer?.apply {
            pause()
            seekTo(0)
        }
        viewModelScope.launch {
            scoreDao.insertScore(
                ScoreRecord(
                    score = gameState.score,
                    date = Date()
                )
            )
            scoreDao.deleteExcessScores()
        }
    }
    
    private fun generateInitialPipes(): List<Pipe> {
        return listOf(generatePipe(500f))
    }
    
    private fun generatePipe(startX: Float = 800f): Pipe {
        val topHeight = Random.nextFloat() * 
            (GameConfig.MAX_PIPE_HEIGHT - GameConfig.MIN_PIPE_HEIGHT) + 
            GameConfig.MIN_PIPE_HEIGHT
        
        val pipeWidth = Random.nextFloat() * 
            (GameConfig.MAX_PIPE_WIDTH - GameConfig.MIN_PIPE_WIDTH) + 
            GameConfig.MIN_PIPE_WIDTH
        
        return Pipe(
            x = startX,
            topHeight = topHeight,
            bottomHeight = 2400f - topHeight - currentPipeGap - GameConfig.GROUND_HEIGHT,
            width = pipeWidth,
            gap = currentPipeGap,
            passed = false
        )
    }
    
    fun navigateToScreen(screen: GameScreen) {
        currentScreen = screen
    }
    
    fun resetGame() {
        gameState = gameState.copy(
            bird = Bird(
                x = 100f,
                y = 300f,
                velocity = 0f,
                rotation = 0f
            ),
            pipes = emptyList(),
            score = 0,
            isGameOver = false,
            isPlaying = false
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
} 