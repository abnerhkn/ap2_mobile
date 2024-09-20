package com.example.monitora

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insert(exercise: Exercise)

    @Query("SELECT * FROM exercise_table ORDER BY id ASC")
    suspend fun getAllExercises(): List<Exercise>
}