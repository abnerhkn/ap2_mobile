package com.example.monitora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListExerciseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var database: ExerciseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_exercise)

        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = ExerciseDatabase.getDatabase(this)

        // Carregar os exercícios do banco de dados
        CoroutineScope(Dispatchers.IO).launch {
            val exercises = database.exerciseDao().getAllExercises()

            // Atualizar a UI no thread principal
            runOnUiThread {
                adapter = ExerciseAdapter(exercises)
                recyclerView.adapter = adapter
            }
        }
    }
}