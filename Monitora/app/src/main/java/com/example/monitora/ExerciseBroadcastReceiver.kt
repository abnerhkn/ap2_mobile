package com.example.monitora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ExerciseBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_HEADSET_PLUG -> {
                // Reagir quando os fones de ouvido forem conectados/desconectados
                val state = intent.getIntExtra("state", -1)
                if (state == 1) {
                    Toast.makeText(context, "Fones conectados! Hora de se exercitar!", Toast.LENGTH_SHORT).show()
                } else if (state == 0) {
                    Toast.makeText(context, "Fones desconectados!", Toast.LENGTH_SHORT).show()
                }
            }

            Intent.ACTION_BATTERY_LOW -> {
                // Reagir quando o nível da bateria estiver baixo
                Toast.makeText(context, "Bateria baixa! Talvez seja melhor pausar o exercício.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}