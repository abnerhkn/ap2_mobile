package com.example.monitora

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val duration: Int,  // duração em minutos
    val distance: Double  // distância em metros
)