package com.example.monitora

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Criar uma notificação para lembrar o usuário de se exercitar
        sendNotification("Lembrete de Exercício", "Está na hora de fazer seu exercício diário!")
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "exercise_reminder_channel"
        val notificationId = System.currentTimeMillis().toInt() // ID dinâmico com base no timestamp atual

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes de Exercício"
            val descriptionText = "Canal para lembretes diários de exercício"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Exemplo de ação ao clicar na notificação (abre a MainActivity)
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Adiciona ação ao clicar
            .setAutoCancel(true) // Cancela a notificação após ser clicada
            .build()

        // Verificação da permissão POST_NOTIFICATIONS (necessário no Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // A permissão não foi concedida, você pode solicitar a permissão ou lidar com a ausência
                return
            }
        }

        // Enviar a notificação
        NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
    }
}
