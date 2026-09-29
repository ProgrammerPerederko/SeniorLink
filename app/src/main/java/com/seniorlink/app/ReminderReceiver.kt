package com.seniorlink.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("seniorlink", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("notifications", true)) return

        val channelId = "seniorlink_reminders"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Lembretes", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Lembretes de medicamentos e compromissos"
                }
            )
        }

        if (Build.VERSION.SDK_INT < 33 || context.checkSelfPermission("android.permission.POST_NOTIFICATIONS") == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_seniorlink)
                .setContentTitle(intent.getStringExtra("title") ?: "Lembrete")
                .setContentText(intent.getStringExtra("description") ?: "Você tem um lembrete.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            manager.notify(intent.getIntExtra("id", 1), notification)
        }

        if (intent.getStringExtra("repeat") == "Diariamente") {
            val next = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
            val nextIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("id", intent.getIntExtra("id", 1))
                putExtra("title", intent.getStringExtra("title"))
                putExtra("description", intent.getStringExtra("description"))
                putExtra("repeat", "Diariamente")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, intent.getIntExtra("id", 1), nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.timeInMillis, pendingIntent)
        }
    }
}