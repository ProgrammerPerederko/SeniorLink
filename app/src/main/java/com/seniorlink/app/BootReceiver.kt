package com.seniorlink.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.json.JSONArray

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val prefs = context.getSharedPreferences("seniorlink", Context.MODE_PRIVATE)
        val array = JSONArray(prefs.getString("reminders", "[]") ?: "[]")
        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            if (!o.optBoolean("active", true) || o.optLong("at") <= System.currentTimeMillis()) continue
            val reminderIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("id", o.getInt("id"))
                putExtra("title", o.getString("title"))
                putExtra("description", o.optString("description"))
                putExtra("repeat", o.optString("repeat", "Não repetir"))
            }
            val pi = PendingIntent.getBroadcast(
                context, o.getInt("id"), reminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, o.getLong("at"), pi)
        }
    }
}