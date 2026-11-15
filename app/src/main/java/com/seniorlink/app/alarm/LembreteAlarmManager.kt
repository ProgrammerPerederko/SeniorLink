package com.seniorlink.app.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.seniorlink.app.R
import java.util.Calendar

object LembreteScheduler {
    fun agendarLembrete(
        context: Context,
        id: String,
        titulo: String,
        horario: String,
        categoria: String,
        idosoId: String
    ) {
        val partes = horario.split(":")
        if (partes.size != 2) return
        val hora = partes[0].toIntOrNull() ?: return
        val minuto = partes[1].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minuto)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, LembreteAlarmReceiver::class.java).apply {
            putExtra("ID", id)
            putExtra("TITULO", titulo)
            putExtra("CATEGORIA", categoria)
            putExtra("HORARIO", horario)
            putExtra("IDOSO_ID", idosoId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelarLembrete(context: Context, id: String) {
        val intent = Intent(context, LembreteAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}

class LembreteAlarmReceiver : BroadcastReceiver() {
    @Suppress("DEPRECATION")
    @SuppressLint("FullScreenIntentPolicy")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("ID") ?: ""
        val titulo = intent.getStringExtra("TITULO") ?: "Hora do seu compromisso!"
        val categoria = intent.getStringExtra("CATEGORIA") ?: "Lembrete"
        val horario = intent.getStringExtra("HORARIO") ?: ""

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "SeniorLink::LembreteWakeLock"
        )
        wakeLock.acquire(10_000L)

        val channelId = "seniorlink_alarmes_urgentes"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lembretes e Remédios (Alarme)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos importantes na tela bloqueada"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 800, 400, 800)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val alarmIntent = Intent(context, AlarmeDisparoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("ID", id)
            putExtra("TITULO", titulo)
            putExtra("CATEGORIA", categoria)
            putExtra("HORARIO", horario)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val somAlarme = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_lembretes)
            .setContentTitle("⏰ $categoria • $horario")
            .setContentText(titulo)
            .setStyle(NotificationCompat.BigTextStyle().bigText(titulo))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(somAlarme)
            .setVibrate(longArrayOf(0, 800, 400, 800))
            .setAutoCancel(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}