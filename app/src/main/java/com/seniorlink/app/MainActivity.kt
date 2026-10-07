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
import com.seniorlink.app.ui.screens.AprenderScreen
import com.seniorlink.app.ui.screens.BoasVindasSeniorLinkScreen
import com.seniorlink.app.ui.screens.CadastroSeniorLinkScreen
import com.seniorlink.app.ui.screens.ConfiguracoesSeniorLinkScreen
import com.seniorlink.app.ui.screens.ContatosConfiancaScreen
import com.seniorlink.app.ui.screens.EmergenciaScreen
import com.seniorlink.app.ui.screens.HomeScreen
import com.seniorlink.app.ui.screens.LembretesScreen
import com.seniorlink.app.ui.screens.LocalizacaoScreen
import com.seniorlink.app.ui.screens.LoginSeniorLinkScreen
import com.seniorlink.app.ui.screens.NovoLembreteScreen
import com.seniorlink.app.ui.screens.PerfilScreen
import com.seniorlink.app.ui.screens.SplashScreen
import com.seniorlink.app.ui.screens.TreinamentoResponsavelScreen
import com.seniorlink.app.ui.theme.SeniorLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val abrirLogin = intent.getBooleanExtra("abrir_login", false)
        val abrirInicio = intent.getBooleanExtra("abrir_inicio", false)

        setContent {
            SeniorLinkTheme {
                SeniorLinkApp(
                    abrirLoginDireto = abrirLogin,
                    abrirInicioDireto = abrirInicio
                )
            }
        }
    }
}

@Composable
fun SeniorLinkApp(
    abrirLoginDireto: Boolean = false,
    abrirInicioDireto: Boolean = false
) {
    var telaAtual by remember {
        mutableStateOf(
            when {
                abrirLoginDireto -> "login"
                abrirInicioDireto -> "inicio"
                else -> "splash"
            }
        )
    }

    BackHandler(
        enabled = telaAtual != "splash" && telaAtual != "inicio"
    ) {
        telaAtual = when (telaAtual) {
            "login" -> "inicio"
            "cadastro" -> "login"
            "boas_vindas" -> "inicio"
            "novo_lembrete", "contatos", "configuracoes",
            "treinamento_responsavel" -> "inicio"
            else -> "inicio"
        }
    }

    when (telaAtual) {
        "splash" -> SplashScreen {
            telaAtual = "login"
        }

        "login" -> LoginSeniorLinkScreen(
            onEntrar = { telaAtual = "inicio" },
            onCriarConta = { telaAtual = "cadastro" },
            onEsqueciSenha = { }
        )

        "cadastro" -> CadastroSeniorLinkScreen(
            onVoltar = { telaAtual = "login" },
            onCadastrar = { telaAtual = "boas_vindas" }
        )

        "boas_vindas" -> BoasVindasSeniorLinkScreen {
            telaAtual = "inicio"
        }

        "inicio" -> HomeScreen(
            onNavigate = { destino ->
                telaAtual = normalizarDestino(destino)
            }
        )

        "perfil" -> PerfilScreen(
            onVoltar = { telaAtual = "inicio" }
        )

        "localizacao" -> LocalizacaoScreen(
            onVoltar = { telaAtual = "inicio" }
        )

        "lembretes" -> LembretesScreen(
            onVoltar = { telaAtual = "inicio" },
            onNovoLembrete = { telaAtual = "novo_lembrete" }
        )

        "novo_lembrete" -> NovoLembreteScreen(
            onVoltar = { telaAtual = "lembretes" },
            onSalvar = { telaAtual = "lembretes" }
        )

        "emergencia" -> EmergenciaScreen(
            onVoltar = { telaAtual = "inicio" }
        )

        "aprender" -> AprenderScreen(
            onVoltar = { telaAtual = "inicio" }
        )

        "contatos" -> ContatosConfiancaScreen(
            onVoltar = { telaAtual = "configuracoes" }
        )

        "configuracoes" -> ConfiguracoesSeniorLinkScreen(
            onVoltar = { telaAtual = "inicio" },
            onPerfil = { telaAtual = "perfil" },
            onContatos = { telaAtual = "contatos" },
            onTreinamentoResponsavel = { telaAtual = "treinamento_responsavel" },
            onLogout = { telaAtual = "login" }
        )

        "treinamento_responsavel" -> TreinamentoResponsavelScreen {
            telaAtual = "configuracoes"
        }

        else -> HomeScreen(
            onNavigate = { destino ->
                telaAtual = normalizarDestino(destino)
            }
        )
    }
}

fun normalizarDestino(destino: String): String {
    return when (
        destino.trim()
            .lowercase()
            .replace(" ", "")
            .replace("ã", "a")
            .replace("ç", "c")
            .replace("ó", "o")
    ) {
        "perfil", "meuperfil" -> "perfil"
        "localizacao" -> "localizacao"
        "lembretes" -> "lembretes"
        "novolembrete", "adicionarlembrete" -> "novo_lembrete"
        "aprender" -> "aprender"
        "emergencia" -> "emergencia"
        "contatos", "contatosdeconfianca" -> "contatos"
        "configuracoes", "configuracao" -> "configuracoes"
        "treinamentoresponsavel", "responsavel" -> "treinamento_responsavel"
        "inicio", "home" -> "inicio"
        else -> "inicio"
    }
}
