package com.example.monitora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurando os botões com seus listeners
        val addExerciseButton = findViewById<Button>(R.id.buttonAddExercise)
        val viewHistoryButton = findViewById<Button>(R.id.buttonViewHistory)
        val startMonitorButton = findViewById<Button>(R.id.buttonStartMonitor)

        addExerciseButton.setOnClickListener {
            // Iniciar a atividade de cadastro de exercício
            startActivity(Intent(this, AddExerciseActivity::class.java))
        }

        viewHistoryButton.setOnClickListener {
            // Iniciar a atividade de histórico de exercícios
            startActivity(Intent(this, ListExerciseActivity::class.java))
        }

        startMonitorButton.setOnClickListener {
            // Iniciar a atividade de monitoramento
            startActivity(Intent(this, MonitorActivity::class.java))

            // Chamar a função para agendar o lembrete de exercício a cada 10 segundos
            scheduleReminderWork()
        }
    }

    private fun scheduleReminderWork() {
        // Definir o trabalho com um atraso de 10 segundos
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        // Agendar o trabalho
        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        // Após a execução, reagendar o trabalho para rodar novamente em 10 segundos
        WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    scheduleReminderWork()
                }
            }
    }
}
