package com.example.monitora

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var database: ExerciseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        val nameInput = findViewById<EditText>(R.id.editTextExerciseName)
        val durationInput = findViewById<EditText>(R.id.editTextDuration)
        val distanceInput = findViewById<EditText>(R.id.editTextDistance)
        val saveButton = findViewById<Button>(R.id.buttonSaveExercise)

        // Inicializando o banco de dados
        database = ExerciseDatabase.getDatabase(this)

        saveButton.setOnClickListener {
            val exerciseName = nameInput.text.toString()
            val duration = durationInput.text.toString().toIntOrNull()
            val distance = distanceInput.text.toString().toDoubleOrNull()

            if (exerciseName.isNotEmpty() && duration != null && distance != null) {
                // Criar o objeto Exercise
                val exercise = Exercise(
                    name = exerciseName,
                    duration = duration,
                    distance = distance
                )

                // Salvar no banco de dados usando coroutines
                CoroutineScope(Dispatchers.IO).launch {
                    database.exerciseDao().insert(exercise)
                }

                Toast.makeText(this, "Exercício salvo!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a Activity e retorna à anterior
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}