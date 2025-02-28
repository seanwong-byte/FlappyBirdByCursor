package com.example.flappybirdbycursor.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "score_records")
data class ScoreRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,
    val date: Date
) 