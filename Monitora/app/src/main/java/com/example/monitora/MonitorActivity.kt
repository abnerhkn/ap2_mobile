package com.example.monitora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MonitorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        val startButton = findViewById<Button>(R.id.buttonStartService)
        val stopButton = findViewById<Button>(R.id.buttonStopService)

        startButton.setOnClickListener {
            val serviceIntent = Intent(this, ExerciseService::class.java)
            startService(serviceIntent)  // Inicia o serviço
        }

        stopButton.setOnClickListener {
            val serviceIntent = Intent(this, ExerciseService::class.java)
            stopService(serviceIntent)  // Para o serviço
        }
    }
}