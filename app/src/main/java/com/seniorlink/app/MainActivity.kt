
package com.seniorlink.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.seniorlink.app.ui.screens.SplashScreen
import com.seniorlink.app.ui.screens.HomeScreen
import com.seniorlink.app.ui.screens.PerfilScreen
import com.seniorlink.app.ui.screens.LocalizacaoScreen
import com.seniorlink.app.ui.screens.LembretesScreen
import com.seniorlink.app.ui.screens.AprenderScreen
import com.seniorlink.app.ui.screens.EmergenciaScreen

import com.seniorlink.app.ui.theme.SeniorLinkTheme

class MainActivity : ComponentActivity() {

    private var iniciarNaHome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iniciarNaHome = intent.getBooleanExtra("abrir_inicio", false)

        setContent {
            SeniorLinkTheme {
                SeniorLinkApp(
                    abrirInicioDireto = iniciarNaHome
                )
            }
        }
    }
}

@Composable
fun SeniorLinkApp(abrirInicioDireto: Boolean = false) {

    var telaAtual by remember {
        mutableStateOf(
            if (abrirInicioDireto) "inicio" else "splash"
        )
    }

    BackHandler(
        enabled = telaAtual != "splash" &&
                telaAtual != "inicio"
    ) {
        telaAtual = "inicio"
    }

    when (telaAtual) {

        "splash" -> {
            SplashScreen(
                onContinuar = {
                    telaAtual = "inicio"
                }
            )
        }

        "inicio" -> {
            HomeScreen(
                onNavigate = { destino ->
                    telaAtual = normalizarDestino(destino)
                }
            )
        }

        "perfil" -> {
            PerfilScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }

        "localizacao" -> {
            LocalizacaoScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }

        "lembretes" -> {
            LembretesScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }

        "aprender" -> {
            AprenderScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }

        "emergencia" -> {
            EmergenciaScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }

        else -> {
            HomeScreen(
                onNavigate = { destino ->
                    telaAtual = normalizarDestino(destino)
                }
            )
        }
    }
}

fun normalizarDestino(destino: String): String {

    return when (
        destino.trim().lowercase()
            .replace(" ", "")
            .replace("ã", "a")
            .replace("ç", "c")
            .replace("ó", "o")
    ) {
        "perfil", "meuperfil" -> "perfil"
        "localizacao" -> "localizacao"
        "lembretes" -> "lembretes"
        "aprender" -> "aprender"
        "emergencia" -> "emergencia"
        "inicio", "home" -> "inicio"
        else -> "inicio"
    }
}