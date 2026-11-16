package com.seniorlink.app.alarm

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.seniorlink.app.data.FirestoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AlarmeDisparoActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var lembreteId: String = ""
    private var autoCloseJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                concluirEFecharAlarme()
            }
        })

        lembreteId = intent.getStringExtra("ID") ?: ""
        val titulo = intent.getStringExtra("TITULO") ?: "Hora do seu compromisso!"
        val categoria = intent.getStringExtra("CATEGORIA") ?: "Lembrete"
        val horario = intent.getStringExtra("HORARIO") ?: ""

        setContent {
            TelaAlarmeBloqueado(
                titulo = titulo,
                categoria = categoria,
                horario = horario,
                onConfirmar = { concluirEFecharAlarme() },
                onAdiar5Minutos = { adiar5Minutos(titulo, categoria) }
            )
        }

        iniciarSomAlarmeAssincrono()
        iniciarTimeoutSeguranca()
    }

    private fun iniciarSomAlarmeAssincrono() {
        try {
            val uriAlarme = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uriAlarme)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                setOnPreparedListener { mp -> mp.start() }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun iniciarTimeoutSeguranca() {
        // Para o alarme automaticamente após 3 minutos se ninguém interagir, evitando toque infinito
        autoCloseJob = lifecycleScope.launch {
            delay(3 * 60 * 1000L)
            concluirEFecharAlarme()
        }
    }

    private fun adiar5Minutos(titulo: String, categoria: String) {
        autoCloseJob?.cancel()
        pararSomAlarme()
        val cal = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 5)
        }
        val novoHorario = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
        val idSoneca = lembreteId.ifBlank { "soneca_${System.currentTimeMillis()}" }
        LembreteScheduler.agendarLembrete(this, idSoneca, titulo, novoHorario, categoria)
        Toast.makeText(this, "Adiado para às $novoHorario", Toast.LENGTH_SHORT).show()
        finishAndRemoveTask()
    }

    private fun concluirEFecharAlarme() {
        autoCloseJob?.cancel()
        pararSomAlarme()
        if (lembreteId.isNotBlank()) {
            val repo = FirestoreRepository()
            lifecycleScope.launch {
                try {
                    repo.alternarConcluidoLembrete(lembreteId, false)
                } catch (_: Exception) {
                } finally {
                    finishAndRemoveTask()
                }
            }
        } else {
            finishAndRemoveTask()
        }
    }

    private fun pararSomAlarme() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.release()
            }
            mediaPlayer = null
        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        autoCloseJob?.cancel()
        pararSomAlarme()
        super.onDestroy()
    }
}

@Composable
private fun TelaAlarmeBloqueado(
    titulo: String,
    categoria: String,
    horario: String,
    onConfirmar: () -> Unit,
    onAdiar5Minutos: () -> Unit
) {
    val azulEscuro = Color(0xFF0A2E4D)
    val azulMedio = Color(0xFF1764A5)
    val verdeMarca = Color(0xFF079B91)

    val iconeCategoria = when (categoria) {
        "Remédio" -> "💊"
        "Consulta" -> "🩺"
        else -> "⏰"
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulsoAlarme")
    val escala by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "escalaIcone"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(azulEscuro, azulMedio)
                )
            )
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            color = Color.White,
            shadowElevation = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Badge da Categoria
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = verdeMarca.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = categoria.uppercase(),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = verdeMarca,
                        letterSpacing = 1.4.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Ícone Pulsante
                Surface(
                    shape = CircleShape,
                    color = verdeMarca.copy(alpha = 0.15f),
                    modifier = Modifier
                        .size(92.dp)
                        .scale(escala)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = iconeCategoria, fontSize = 46.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Horário em destaque
                Text(
                    text = horario,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = azulEscuro
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Card interno destacando o título do lembrete
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF3F7FA),
                    border = BorderStroke(1.dp, Color(0xFFDCE6EE))
                ) {
                    Text(
                        text = titulo,
                        modifier = Modifier.padding(18.dp),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF102A43),
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Botão Principal (Concluir)
                Button(
                    onClick = onConfirmar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = verdeMarca),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "✓ JÁ FIZ / CONCLUIR",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botão Secundário (Adiar 5 minutos)
                OutlinedButton(
                    onClick = onAdiar5Minutos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.5.dp, azulMedio.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "⏰ Lembrar novamente em 5 min",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulEscuro
                    )
                }
            }
        }
    }
}