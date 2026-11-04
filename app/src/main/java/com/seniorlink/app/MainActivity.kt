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
import com.seniorlink.app.ui.screens.CuidadorScreen
import com.seniorlink.app.ui.screens.EmergenciaScreen
import com.seniorlink.app.ui.screens.HomeScreen
import com.seniorlink.app.ui.screens.JogosScreen
import com.seniorlink.app.ui.screens.LembretesScreen
import com.seniorlink.app.ui.screens.LocalizacaoScreen
import com.seniorlink.app.ui.screens.LoginSeniorLinkScreen
import com.seniorlink.app.ui.screens.NovoLembreteScreen
import com.seniorlink.app.ui.screens.PerfilScreen
import com.seniorlink.app.ui.screens.ResponsavelScreen
import com.seniorlink.app.ui.screens.SplashScreen
import com.seniorlink.app.ui.screens.TreinamentoResponsavelScreen
import com.seniorlink.app.ui.theme.SeniorLinkTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val abrirOnboarding =
            intent.getBooleanExtra("abrir_onboarding", false)

        val abrirLogin =
            intent.getBooleanExtra("abrir_login", false)

        val abrirInicio =
            intent.getBooleanExtra("abrir_inicio", false)

        setContent {
            SeniorLinkTheme {
                SeniorLinkApp(
                    abrirOnboardingDireto = abrirOnboarding,
                    abrirLoginDireto = abrirLogin,
                    abrirInicioDireto = abrirInicio
                )
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

    var telaAtual by remember {

        mutableStateOf(

            when {

                abrirOnboardingDireto ->
                    "onboarding"

                abrirLoginDireto ->
                    "login"

                abrirInicioDireto ->
                    "inicio"

                else ->
                    "onboarding"
            }
        )
    }


    // ============================================================
    // BOTÃO VOLTAR DO ANDROID
    // ============================================================

    BackHandler(
        enabled =
            telaAtual != "onboarding" &&
                    telaAtual != "inicio"
    ) {

        telaAtual = when (telaAtual) {

            // ----------------------------
            // ACESSO
            // ----------------------------

            "login" ->
                "onboarding"

            "cadastro" ->
                "login"

            "boas_vindas" ->
                "inicio"


            // ----------------------------
            // RESPONSÁVEL
            // ----------------------------

            "responsavel" ->
                "login"

            "responsavel_localizacao" ->
                "responsavel"

            "responsavel_ficha" ->
                "responsavel"

            "responsavel_lembretes" ->
                "responsavel"

            "responsavel_novo_lembrete" ->
                "responsavel_lembretes"

            "responsavel_emergencia" ->
                "responsavel"

            "responsavel_treinamento" ->
                "responsavel"


            // ----------------------------
            // CUIDADOR
            // ----------------------------

            "cuidador" ->
                "login"

            "cuidador_agenda" ->
                "cuidador"

            "cuidador_localizacao" ->
                "cuidador"

            "cuidador_emergencia" ->
                "cuidador"

            "cuidador_orientacoes" ->
                "cuidador"


            // ----------------------------
            // IDOSO
            // ----------------------------

            "perfil" ->
                "inicio"

            "localizacao" ->
                "inicio"

            "lembretes" ->
                "inicio"

            "novo_lembrete" ->
                "lembretes"

            "emergencia" ->
                "inicio"

            "aprender" ->
                "inicio"

            "jogos" ->
                "inicio"


            // ----------------------------
            // CONFIGURAÇÕES
            // ----------------------------

            "contatos" ->
                "configuracoes"

            "configuracoes" ->
                "inicio"

            "treinamento_responsavel" ->
                "configuracoes"


            else ->
                "inicio"
        }
    }


    // ============================================================
    // NAVEGAÇÃO
    // ============================================================

    when (telaAtual) {


        // ========================================================
        // ONBOARDING
        // ========================================================

        "onboarding" -> {

            SplashScreen(
                onContinuar = {
                    telaAtual = "login"
                }
            )
        }


        // ========================================================
        // LOGIN
        // ========================================================

        "login" -> {

            LoginSeniorLinkScreen(

                // IDOSO
                onEntrar = {
                    telaAtual = "inicio"
                },

                // RESPONSÁVEL
                onEntrarResponsavel = {
                    telaAtual = "responsavel"
                },

                // CUIDADOR
                onEntrarCuidador = {
                    telaAtual = "cuidador"
                },

                // CADASTRO
                onCriarConta = {
                    telaAtual = "cadastro"
                },

                // AJUDA
                onEsqueciSenha = {
                    // Próxima etapa.
                }
            )
        }


        // ========================================================
        // CADASTRO
        // ========================================================

        "cadastro" -> {

            CadastroSeniorLinkScreen(

                onVoltar = {
                    telaAtual = "login"
                },

                onCadastrar = {
                    telaAtual = "boas_vindas"
                }
            )
        }


        // ========================================================
        // BOAS-VINDAS
        // ========================================================

        "boas_vindas" -> {

            BoasVindasSeniorLinkScreen {
                telaAtual = "inicio"
            }
        }


        // ========================================================
        // HOME DO IDOSO
        // ========================================================

        "inicio" -> {

            HomeScreen(
                onNavigate = { destino ->

                    telaAtual =
                        normalizarDestino(destino)
                }
            )
        }


        // ========================================================
        // ÁREA DO RESPONSÁVEL
        // ========================================================

        "responsavel" -> {

            ResponsavelScreen(

                onVoltar = {
                    telaAtual = "login"
                },

                onLocalizacao = {
                    telaAtual = "responsavel_localizacao"
                },

                onFichaIdoso = {
                    telaAtual = "responsavel_ficha"
                },

                onLembretes = {
                    telaAtual = "responsavel_lembretes"
                },

                onContatos = {
                    // Mantido apenas para compatibilidade
                    // com a função existente.
                    telaAtual = "responsavel"
                },

                onEmergencia = {
                    telaAtual = "responsavel_emergencia"
                },

                onTreinamento = {
                    telaAtual = "responsavel_treinamento"
                }
            )
        }


        // ========================================================
        // LOCALIZAÇÃO DO RESPONSÁVEL
        // ========================================================

        "responsavel_localizacao" -> {

            LocalizacaoScreen(
                onVoltar = {
                    telaAtual = "responsavel"
                }
            )
        }


        // ========================================================
        // FICHA DO IDOSO
        // ========================================================

        "responsavel_ficha" -> {

            PerfilScreen(
                onVoltar = {
                    telaAtual = "responsavel"
                }
            )
        }


        // ========================================================
        // LEMBRETES DO RESPONSÁVEL
        // ========================================================

        "responsavel_lembretes" -> {

            LembretesScreen(

                onVoltar = {
                    telaAtual = "responsavel"
                },

                onNovoLembrete = {
                    telaAtual =
                        "responsavel_novo_lembrete"
                }
            )
        }


        // ========================================================
        // NOVO LEMBRETE DO RESPONSÁVEL
        // ========================================================

        "responsavel_novo_lembrete" -> {

            NovoLembreteScreen(

                onVoltar = {
                    telaAtual =
                        "responsavel_lembretes"
                },

                onSalvar = {
                    telaAtual =
                        "responsavel_lembretes"
                }
            )
        }


        // ========================================================
        // EMERGÊNCIA DO RESPONSÁVEL
        // ========================================================

        "responsavel_emergencia" -> {

            EmergenciaScreen(
                onVoltar = {
                    telaAtual = "responsavel"
                }
            )
        }


        // ========================================================
        // TREINAMENTO DO RESPONSÁVEL
        // ========================================================

        "responsavel_treinamento" -> {

            TreinamentoResponsavelScreen {
                telaAtual = "responsavel"
            }
        }


        // ========================================================
        // ÁREA DO CUIDADOR
        // ========================================================

        "cuidador" -> {

            CuidadorScreen(

                onVoltar = {
                    telaAtual = "login"
                },

                onAgenda = {
                    telaAtual = "cuidador_agenda"
                },

                onLocalizacao = {
                    telaAtual = "cuidador_localizacao"
                },

                onEmergencia = {
                    telaAtual = "cuidador_emergencia"
                },

                onOrientacoes = {
                    telaAtual = "cuidador_orientacoes"
                }
            )
        }


        // ========================================================
        // AGENDA DO CUIDADOR
        // ========================================================

        "cuidador_agenda" -> {

            LembretesScreen(

                onVoltar = {
                    telaAtual = "cuidador"
                },

                onNovoLembrete = {
                    telaAtual = "responsavel_novo_lembrete"
                }
            )
        }


        // ========================================================
        // LOCALIZAÇÃO DO CUIDADOR
        // ========================================================

        "cuidador_localizacao" -> {

            LocalizacaoScreen(
                onVoltar = {
                    telaAtual = "cuidador"
                }
            )
        }


        // ========================================================
        // EMERGÊNCIA DO CUIDADOR
        // ========================================================

        "cuidador_emergencia" -> {

            EmergenciaScreen(
                onVoltar = {
                    telaAtual = "cuidador"
                }
            )
        }


        // ========================================================
        // ORIENTAÇÕES DO CUIDADOR
        // ========================================================

        "cuidador_orientacoes" -> {

            TreinamentoResponsavelScreen {
                telaAtual = "cuidador"
            }
        }


        // ========================================================
        // PERFIL DO IDOSO
        // ========================================================

        "perfil" -> {

            PerfilScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }


        // ========================================================
        // LOCALIZAÇÃO DO IDOSO
        // ========================================================

        "localizacao" -> {

            LocalizacaoScreen(
                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }


        // ========================================================
        // LEMBRETES DO IDOSO
        // ========================================================

        "lembretes" -> {

            LembretesScreen(

                onVoltar = {
                    telaAtual = "inicio"
                },

                onNovoLembrete = {
                    telaAtual = "novo_lembrete"
                }
            )
        }


        // ========================================================
        // NOVO LEMBRETE DO IDOSO
        // ========================================================

        "novo_lembrete" -> {

            NovoLembreteScreen(

                onVoltar = {
                    telaAtual = "lembretes"
                },

                onSalvar = {
                    telaAtual = "lembretes"
                }
            )
        }


        // ========================================================
        // EMERGÊNCIA DO IDOSO
        // ========================================================

        "emergencia" -> {

            EmergenciaScreen(

                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }


        // ========================================================
        // APRENDER
        // ========================================================

        "aprender" -> {

            AprenderScreen(

                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }


        // ========================================================
        // JOGOS
        // ========================================================

        "jogos" -> {

            JogosScreen(

                onVoltar = {
                    telaAtual = "inicio"
                }
            )
        }


        // ========================================================
        // CONTATOS
        // ========================================================

        "contatos" -> {

            ContatosConfiancaScreen(

                onVoltar = {
                    telaAtual = "configuracoes"
                }
            )
        }


        // ========================================================
        // CONFIGURAÇÕES
        // ========================================================

        "configuracoes" -> {

            ConfiguracoesSeniorLinkScreen(

                onVoltar = {
                    telaAtual = "inicio"
                },

                onPerfil = {
                    telaAtual = "perfil"
                },

                onContatos = {
                    telaAtual = "contatos"
                },

                onTreinamentoResponsavel = {
                    telaAtual =
                        "treinamento_responsavel"
                },

                onLogout = {
                    telaAtual = "login"
                }
            )
        }


        // ========================================================
        // TREINAMENTO DAS CONFIGURAÇÕES
        // ========================================================

        "treinamento_responsavel" -> {

            TreinamentoResponsavelScreen {

                telaAtual = "configuracoes"
            }
        }


        // ========================================================
        // FALLBACK
        // ========================================================

        else -> {

            telaAtual = "inicio"
        }
    }
}


// ================================================================
// NORMALIZAÇÃO DOS DESTINOS DA HOME
// ================================================================

fun normalizarDestino(
    destino: String
): String {

    val destinoNormalizado = destino
        .trim()
        .lowercase()
        .replace(" ", "")
        .replace("ã", "a")
        .replace("á", "a")
        .replace("â", "a")
        .replace("à", "a")
        .replace("ç", "c")
        .replace("é", "e")
        .replace("ê", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ô", "o")
        .replace("õ", "o")
        .replace("ú", "u")

    return when (destinoNormalizado) {

        "perfil",
        "meuperfil" ->
            "perfil"

        "localizacao" ->
            "localizacao"

        "lembretes" ->
            "lembretes"

        "novolembrete",
        "adicionarlembrete" ->
            "novo_lembrete"

        "aprender" ->
            "aprender"

        "emergencia" ->
            "emergencia"

        "contatos",
        "contatosdeconfianca" ->
            "contatos"

        "configuracoes",
        "configuracao" ->
            "configuracoes"

        "treinamentoresponsavel" ->
            "treinamento_responsavel"

        "jogos",
        "jogo",
        "cacapalavras" ->
            "jogos"

        "inicio",
        "home" ->
            "inicio"

        else ->
            "inicio"
    }
}
