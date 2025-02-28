package com.example.flappybirdbycursor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.flappybirdbycursor.model.ScoreRecord

@Database(entities = [ScoreRecord::class], version = 1)
@TypeConverters(Converters::class)
abstract class ScoreDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: ScoreDatabase? = null

        fun getDatabase(context: Context): ScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "score_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 