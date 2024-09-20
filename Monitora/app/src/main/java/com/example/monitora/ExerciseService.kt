package com.example.monitora

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var isRunning = false
    private var steps = 0  // Contador de passos
    private val handler = Handler(Looper.getMainLooper())
    private var elapsedTime = 0  // Contador de tempo em segundos
    private lateinit var database: ExerciseDatabase

    override fun onCreate() {
        super.onCreate()

        // Inicializar o gerenciador de sensores e o acelerômetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Inicializar o banco de dados
        database = ExerciseDatabase.getDatabase(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Monitoramento de exercício iniciado", Toast.LENGTH_SHORT).show()
        isRunning = true
        startStepCounting()
        startTimer()
        return START_STICKY
    }

    private fun startStepCounting() {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun startTimer() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning) {
                    elapsedTime++
                    handler.postDelayed(this, 1000)  // Incrementa o tempo a cada segundo
                }
            }
        }, 1000)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Verifica se há movimento significativo para contar um passo
            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH
            if (acceleration > 2) {  // Valor ajustável para detecção de movimento
                steps++
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Não precisamos implementar isso para o acelerômetro
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)  // Para o monitoramento de sensores
        Toast.makeText(this, "Monitoramento de exercício finalizado", Toast.LENGTH_SHORT).show()

        // Salvar o progresso no banco de dados
        saveExerciseData()
        handler.removeCallbacksAndMessages(null)  // Para o contador
    }

    private fun saveExerciseData() {
        val exercise = Exercise(
            name = "Exercício Monitorado",
            duration = elapsedTime / 60,  // Tempo em minutos
            distance = steps * 0.78  // Distância estimada (passos * comprimento médio do passo)
        )

        // Salvar no banco de dados usando coroutines
        CoroutineScope(Dispatchers.IO).launch {
            database.exerciseDao().insert(exercise)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}