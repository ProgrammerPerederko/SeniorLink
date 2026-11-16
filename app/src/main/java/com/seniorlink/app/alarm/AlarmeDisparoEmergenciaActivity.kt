package com.seniorlink.app.alarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.ui.theme.SeniorLinkTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class AlarmeDisparoEmergenciaActivity : ComponentActivity() {
    private var ringtone: Ringtone? = null
    private var autoCloseJob: Job? = null
    private var dbRef: DatabaseReference? = null
    private var firebaseListener: ValueEventListener? = null

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val idosoId = intent.getStringExtra("IDOSO_ID") ?: ""
        val nomeIdoso = intent.getStringExtra("NOME_IDOSO") ?: "Idoso"

        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (idosoId.isNotBlank()) {
            dbRef = FirebaseDatabase.getInstance().getReference("emergencias").child(idosoId).child("ativo")
            firebaseListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ativo = snapshot.getValue(Boolean::class.java) == true
                    if (!ativo) {
                        pararAlarmeEFechar()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            dbRef?.addValueEventListener(firebaseListener!!)
        }

        autoCloseJob = lifecycleScope.launch {
            delay(1.minutes)
            pararAlarmeEFechar()
        }

        setContent {
            SeniorLinkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFD63636))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🚨 PEDIDO DE AJUDA URGENTE!",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "$nomeIdoso acionou o botão de emergência!",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = {
                                    if (idosoId.isNotBlank()) {
                                        FirebaseDatabase.getInstance().getReference("emergencias").child(idosoId).child("ativo").setValue(false)
                                        FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId).child("emergencia_ativa").child("ativo").setValue(false)
                                    }
                                    pararAlarmeEFechar()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text(
                                    text = "PARAR ALARME E ATENDER",
                                    color = Color(0xFFD63636),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pararAlarmeEFechar() {
        autoCloseJob?.cancel()
        if (dbRef != null && firebaseListener != null) {
            dbRef?.removeEventListener(firebaseListener!!)
        }
        try {
            ringtone?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finishAndRemoveTask()
    }

    override fun onDestroy() {
        autoCloseJob?.cancel()
        if (dbRef != null && firebaseListener != null) {
            dbRef?.removeEventListener(firebaseListener!!)
        }
        super.onDestroy()
        try {
            ringtone?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}