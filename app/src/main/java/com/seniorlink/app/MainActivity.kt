package com.seniorlink.app

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

// Imports explícitos de todas as telas do projeto
import com.seniorlink.app.ui.screens.HomeScreen
import com.seniorlink.app.ui.screens.PerfilIdosoScreen
import com.seniorlink.app.ui.screens.PainelResponsavelScreen
import com.seniorlink.app.ui.screens.LoginSeniorLinkScreen
import com.seniorlink.app.ui.screens.CadastroSeniorLinkScreen
import com.seniorlink.app.ui.screens.SplashScreen
import com.seniorlink.app.ui.screens.LocalizacaoScreen
import com.seniorlink.app.ui.screens.PerfilScreen
import com.seniorlink.app.ui.screens.LembretesScreen
import com.seniorlink.app.ui.screens.ContatosConfiancaScreen
import com.seniorlink.app.ui.screens.NovoLembreteScreen
import com.seniorlink.app.ui.screens.EmergenciaScreen
import com.seniorlink.app.ui.screens.TreinamentoResponsavelScreen
import com.seniorlink.app.ui.screens.CuidadorScreen
import com.seniorlink.app.ui.screens.AprenderScreen
import com.seniorlink.app.ui.screens.JogosScreen
import com.seniorlink.app.ui.screens.ConfiguracoesSeniorLinkScreen
import com.seniorlink.app.ui.theme.SeniorLinkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        com.seniorlink.app.model.SeniorLinkPrefs.inicializar(applicationContext)

        val abrirOnboarding = intent.getBooleanExtra("abrir_onboarding", false)
        val abrirLogin = intent.getBooleanExtra("abrir_login", false)
        val abrirInicio = intent.getBooleanExtra("abrir_inicio", false)

        setContent {
            SeniorLinkTheme {
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    SeniorLinkApp(
                        abrirOnboardingDireto = abrirOnboarding,
                        abrirLoginDireto = abrirLogin,
                        abrirInicioDireto = abrirInicio
                    )
                }
            }
        }
    }
}

@Composable
fun SeniorLinkApp(
    abrirOnboardingDireto: Boolean = false,
    abrirLoginDireto: Boolean = false,
    abrirInicioDireto: Boolean = false
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }
    val onboardingJaVisto = remember { prefs.getBoolean("onboarding_concluido", false) }

    val usuarioLogado = remember { FirebaseAuth.getInstance().currentUser }

    // Usando rememberSaveable para manter a tela atual mesmo quando o celular é girado
    var telaAtual by rememberSaveable {
        mutableStateOf(
            when {
                abrirOnboardingDireto -> "onboarding"
                abrirLoginDireto -> "login"
                abrirInicioDireto -> "inicio"
                !onboardingJaVisto -> "onboarding"
                usuarioLogado != null -> "inicio"
                else -> "login"
            }
        )
    }

    var idosoIdParaMapa by remember { mutableStateOf("") }
    var idosoIdSelecionadoResp by remember { mutableStateOf("") }

    val telasRaiz = setOf("onboarding", "login", "inicio", "responsavel", "cuidador")

    BackHandler(enabled = telaAtual !in telasRaiz) {
        telaAtual = when (telaAtual) {
            "cadastro", "responsavel_adicionar_idoso" -> "login"
            "responsavel_localizacao",
            "responsavel_ficha",
            "responsavel_lembretes",
            "responsavel_contatos",
            "responsavel_emergencia",
            "responsavel_treinamento" -> "responsavel"
            "responsavel_novo_lembrete" -> "responsavel_lembretes"
            "cuidador_agenda",
            "cuidador_localizacao",
            "cuidador_emergencia",
            "cuidador_orientacoes" -> "cuidador"
            "novo_lembrete" -> "lembretes"
            "perfil",
            "localizacao",
            "lembretes",
            "emergencia",
            "aprender",
            "jogos",
            "contatos",
            "configuracoes" -> "inicio"
            "configuracoes_contatos",
            "treinamento_responsavel" -> "configuracoes"
            else -> "inicio"
        }
    }

    when (telaAtual) {
        "onboarding" -> {
            SplashScreen(
                onContinuar = {
                    prefs.edit {
                        putBoolean("onboarding_concluido", true)
                    }
                    telaAtual = "login"
                }
            )
        }

        "login" -> {
            LoginSeniorLinkScreen(
                onEntrar = { telaAtual = "inicio" },
                onEntrarResponsavel = { telaAtual = "responsavel" },
                onEntrarCuidador = { telaAtual = "cuidador" },
                onCriarConta = { telaAtual = "cadastro" },
                onEsqueciSenha = { /* Próxima etapa */ }
            )
        }

        "cadastro" -> {
            CadastroSeniorLinkScreen(
                onVoltar = { telaAtual = "login" },
                onEntrarIdoso = { telaAtual = "inicio" },
                onEntrarResponsavel = { telaAtual = "responsavel" },
                onEntrarCuidador = { telaAtual = "cuidador" }
            )
        }

        "inicio" -> {
            HomeScreen(
                onNavigate = { destino ->
                    telaAtual = normalizarDestino(destino)
                }
            )
        }

        "responsavel" -> {
            PainelResponsavelScreen(
                onVoltar = {
                    FirebaseAuth.getInstance().signOut()
                    telaAtual = "login"
                },
                onIrParaLocalizacao = { idIdoso ->
                    idosoIdParaMapa = idIdoso
                    telaAtual = "responsavel_localizacao"
                },
                onIrParaAgenda = { idIdoso ->
                    idosoIdSelecionadoResp = idIdoso
                    telaAtual = "responsavel_lembretes"
                },
                onIrParaFicha = { telaAtual = "responsavel_ficha" },
                onIrParaContatos = { idIdoso ->
                    idosoIdSelecionadoResp = idIdoso
                    telaAtual = "responsavel_contatos"
                },
                onIrParaTreinamento = { telaAtual = "responsavel_treinamento" },
                onAdicionarIdoso = { telaAtual = "responsavel_adicionar_idoso" }
            )
        }

        "responsavel_adicionar_idoso" -> {
            CadastroSeniorLinkScreen(
                onVoltar = { telaAtual = "responsavel" },
                onEntrarIdoso = { telaAtual = "responsavel" },
                onEntrarResponsavel = { telaAtual = "responsavel" },
                onEntrarCuidador = { telaAtual = "responsavel" }
            )
        }

        "responsavel_localizacao" -> {
            LocalizacaoScreen(
                idosoId = idosoIdParaMapa,
                onVoltar = {
                    idosoIdParaMapa = ""
                    telaAtual = "responsavel"
                }
            )
        }

        "responsavel_ficha" -> PerfilScreen(onVoltar = { telaAtual = "responsavel" })

        "responsavel_lembretes" -> {
            LembretesScreen(
                idosoId = idosoIdSelecionadoResp,
                onVoltar = { telaAtual = "responsavel" },
                onNovoLembrete = { telaAtual = "responsavel_novo_lembrete" }
            )
        }

        "responsavel_contatos" -> {
            ContatosConfiancaScreen(
                idosoId = idosoIdSelecionadoResp,
                onVoltar = { telaAtual = "responsavel" }
            )
        }

        "responsavel_novo_lembrete" -> {
            NovoLembreteScreen(
                onVoltar = { telaAtual = "responsavel_lembretes" },
                onSalvar = { telaAtual = "responsavel_lembretes" }
            )
        }

        "responsavel_emergencia" -> EmergenciaScreen(onVoltar = { telaAtual = "responsavel" })
        "responsavel_treinamento" -> TreinamentoResponsavelScreen { telaAtual = "responsavel" }

        "cuidador" -> {
            CuidadorScreen(
                onVoltar = {
                    FirebaseAuth.getInstance().signOut()
                    telaAtual = "login"
                },
                onAgenda = { telaAtual = "cuidador_agenda" },
                onLocalizacao = { telaAtual = "cuidador_localizacao" },
                onEmergencia = { telaAtual = "cuidador_emergencia" },
                onOrientacoes = { telaAtual = "cuidador_orientacoes" }
            )
        }

        "cuidador_agenda" -> {
            LembretesScreen(
                onVoltar = { telaAtual = "cuidador" },
                onNovoLembrete = { telaAtual = "responsavel_novo_lembrete" }
            )
        }

        "cuidador_localizacao" -> LocalizacaoScreen(onVoltar = { telaAtual = "cuidador" })
        "cuidador_emergencia" -> EmergenciaScreen(onVoltar = { telaAtual = "cuidador" })
        "cuidador_orientacoes" -> TreinamentoResponsavelScreen { telaAtual = "cuidador" }

        "perfil" -> PerfilIdosoScreen(onVoltar = { telaAtual = "inicio" })

        "localizacao" -> LocalizacaoScreen(onVoltar = { telaAtual = "inicio" })

        "lembretes" -> {
            LembretesScreen(
                onVoltar = { telaAtual = "inicio" },
                onNovoLembrete = { telaAtual = "novo_lembrete" }
            )
        }

        "novo_lembrete" -> {
            NovoLembreteScreen(
                onVoltar = { telaAtual = "lembretes" },
                onSalvar = { telaAtual = "lembretes" }
            )
        }

        "emergencia" -> EmergenciaScreen(onVoltar = { telaAtual = "inicio" })
        "aprender" -> AprenderScreen(onVoltar = { telaAtual = "inicio" })
        "jogos" -> JogosScreen(onVoltar = { telaAtual = "inicio" })
        "contatos" -> ContatosConfiancaScreen(onVoltar = { telaAtual = "inicio" })
        "configuracoes_contatos" -> ContatosConfiancaScreen(onVoltar = { telaAtual = "configuracoes" })

        "configuracoes" -> {
            ConfiguracoesSeniorLinkScreen(
                onVoltar = { telaAtual = "inicio" },
                onPerfil = { telaAtual = "perfil" },
                onContatos = { telaAtual = "configuracoes_contatos" },
                onTreinamentoResponsavel = { telaAtual = "treinamento_responsavel" },
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    telaAtual = "login"
                }
            )
        }

        "treinamento_responsavel" -> TreinamentoResponsavelScreen { telaAtual = "configuracoes" }
        else -> telaAtual = "inicio"
    }
}

fun normalizarDestino(destino: String): String {
    val destinoNormalizado = destino
        .trim().lowercase().replace(" ", "")
        .replace("ã", "a").replace("á", "a").replace("â", "a").replace("à", "a")
        .replace("ç", "c").replace("é", "e").replace("ê", "e").replace("í", "i")
        .replace("ó", "o").replace("ô", "o").replace("õ", "o").replace("ú", "u")

    return when (destinoNormalizado) {
        "perfil", "meuperfil" -> "perfil"
        "localizacao" -> "localizacao"
        "lembretes" -> "lembretes"
        "novolembrete", "adicionarlembrete" -> "novo_lembrete"
        "aprender" -> "aprender"
        "emergencia" -> "emergencia"
        "contatos", "contatosdeconfianca" -> "contatos"
        "configuracoes", "configuracao" -> "configuracoes"
        "treinamentoresponsavel" -> "treinamento_responsavel"
        "jogos", "jogo", "cacapalavras" -> "jogos"
        "inicio", "home" -> "inicio"
        else -> "inicio"
    }
}