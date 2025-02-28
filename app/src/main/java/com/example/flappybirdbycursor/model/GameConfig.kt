package com.example.flappybirdbycursor.model

object GameConfig {
    const val GRAVITY = 0.8f
    const val JUMP_FORCE = -15f
    const val BIRD_WIDTH = 60f
    const val BIRD_HEIGHT = 40f
    const val PIPE_SPEED = 5f
    const val PIPE_SPACING = 300f
    
    // 初始管道间隙和最小管道间隙
    const val INITIAL_PIPE_GAP = 250f
    const val MIN_PIPE_GAP = 180f
    const val GAP_DECREASE_RATE = 0.98f  // 每通过一个管道，间隙缩小的比率
    
    const val GROUND_HEIGHT = 100f
    
    // 碰撞检测的容差
    const val COLLISION_TOLERANCE = 5f
    
    // 管道生成的范围
    const val MIN_PIPE_HEIGHT = 150f
    const val MAX_PIPE_HEIGHT = 350f
    
    // 管道宽度范围
    const val MIN_PIPE_WIDTH = 80f
    const val MAX_PIPE_WIDTH = 120f
} 