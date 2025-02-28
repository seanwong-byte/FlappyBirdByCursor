package com.example.flappybirdbycursor.model

data class Pipe(
    val x: Float,
    val topHeight: Float,
    val bottomHeight: Float,
    val width: Float,
    val gap: Float,
    val passed: Boolean = false
) 