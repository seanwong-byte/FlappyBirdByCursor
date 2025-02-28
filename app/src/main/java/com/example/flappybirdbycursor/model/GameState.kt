package com.example.flappybirdbycursor.model

data class Bird(
    val x: Float,
    val y: Float,
    val velocity: Float,
    val rotation: Float
)

data class GameState(
    val bird: Bird,
    val pipes: List<Pipe>,
    val score: Int,
    val isGameOver: Boolean,
    val isPlaying: Boolean
)

enum class GameScreen {
    WELCOME,
    GAME,
    HISTORY
} 