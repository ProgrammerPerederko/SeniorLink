package com.seniorlink.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.MainActivity
import com.seniorlink.app.R
import com.seniorlink.app.alarm.AlarmeDisparoEmergenciaActivity

class AlertaBackgroundService : Service() {
    private val db = FirebaseDatabase.getInstance()
    private val activeListeners = mutableListOf<Pair<com.google.firebase.database.DatabaseReference, ValueEventListener>>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        criarCanalNotificacao()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, "canal_emergencia_seniorlink")
            .setContentTitle("SeniorLink Monitorando")
            .setContentText("Proteção de emergência ativa em segundo plano")
            .setSmallIcon(R.drawable.icon_idoso)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
        observarEmergenciasGlobalmente()

        return START_STICKY
    }

    private fun observarEmergenciasGlobalmente() {
        val emailResponsavel = FirebaseAuth.getInstance().currentUser?.email ?: return

        db.getReference("usuarios").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { doc ->
                    val tipo = doc.child("tipo").getValue(String::class.java)
                    val emailResp = doc.child("emailResponsavel").getValue(String::class.java)

                    if (tipo == "idoso" && emailResp.equals(emailResponsavel, ignoreCase = true)) {
                        val idosoId = doc.key ?: return@forEach
                        val nomeIdoso = doc.child("nome").getValue(String::class.java) ?: "Idoso"
                        monitorarIdoso(idosoId, nomeIdoso)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun monitorarIdoso(idosoId: String, nomeIdoso: String) {
        val refEmergencia = db.getReference("emergencias").child(idosoId)
        val refUsuarioAtivo = db.getReference("usuarios").child(idosoId).child("emergencia_ativa")

        // Variáveis para controlar a transição e evitar disparos repetidos em loop
        var ultimoEstadoEmergencia = false
        var ultimoEstadoUsuario = false

        val listenerEmergencia = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ativo = snapshot.child("ativo").getValue(Boolean::class.java) == true
                // Só dispara se mudou de falso para verdadeiro
                if (ativo && !ultimoEstadoEmergencia) {
                    dispararTelaEmergencia(idosoId, nomeIdoso)
                }
                ultimoEstadoEmergencia = ativo
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        val listenerUsuario = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ativo = snapshot.child("ativo").getValue(Boolean::class.java) == true
                // Só dispara se mudou de falso para verdadeiro
                if (ativo && !ultimoEstadoUsuario) {
                    dispararTelaEmergencia(idosoId, nomeIdoso)
                }
                ultimoEstadoUsuario = ativo
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        refEmergencia.addValueEventListener(listenerEmergencia)
        refUsuarioAtivo.addValueEventListener(listenerUsuario)

        activeListeners.add(refEmergencia to listenerEmergencia)
        activeListeners.add(refUsuarioAtivo to listenerUsuario)
    }

    private fun dispararTelaEmergencia(idosoId: String, nomeIdoso: String) {
        val intent = Intent(applicationContext, AlarmeDisparoEmergenciaActivity::class.java).apply {
            putExtra("IDOSO_ID", idosoId)
            putExtra("NOME_IDOSO", nomeIdoso)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        applicationContext.startActivity(intent)
    }

    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_emergencia_seniorlink",
                "Alertas de Emergência SeniorLink",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para alertas urgentes de idosos"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        activeListeners.forEach { (ref, listener) ->
            ref.removeEventListener(listener)
        }
        super.onDestroy()
    }
}