package com.example.flappybirdbycursor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flappybirdbycursor.model.ScoreRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score_records ORDER BY score DESC LIMIT 5")
    fun getTopScores(): Flow<List<ScoreRecord>>

    @Insert
    suspend fun insertScore(score: ScoreRecord)

    @Query("SELECT COUNT(*) FROM score_records")
    suspend fun getScoreCount(): Int

    @Query("DELETE FROM score_records WHERE id NOT IN (SELECT id FROM score_records ORDER BY score DESC LIMIT 5)")
    suspend fun deleteExcessScores()
} 