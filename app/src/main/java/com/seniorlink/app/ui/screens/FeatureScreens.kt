
package com.seniorlink.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seniorlink.app.R


// ============================================================
// CORES
// ============================================================

private val AzulPrincipal = Color(0xFF1764A5)
private val AzulEscuro = Color(0xFF123B63)
private val AzulClaro = Color(0xFFEAF4FC)

private val Verde = Color(0xFF079B91)
private val VerdeClaro = Color(0xFFE8F7F5)

private val LaranjaClaro = Color(0xFFFFF2DF)

private val Vermelho = Color(0xFFE94B4B)
private val VermelhoClaro = Color(0xFFFFF0F0)

private val Fundo = Color(0xFFF7F9FC)
private val Branco = Color.White

private val Texto = Color(0xFF18334D)
private val TextoSecundario = Color(0xFF66788A)

private val Borda = Color(0xFFE1E8EF)


// ============================================================
// PERFIL
// ============================================================

@Composable
fun PerfilScreen(
    onVoltar: () -> Unit
) {

    var nome by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var sangue by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var condicoes by remember { mutableStateOf("") }
    var contato by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Meu perfil",
            subtitulo = "Informações importantes para sua segurança.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(26.dp))

        CardSecao("Informações pessoais") {

            Campo("Nome completo", nome) {
                nome = it
            }

            Campo("Data de nascimento", nascimento) {
                nascimento = it
            }
        }

        CardSecao("Informações de saúde") {

            Campo("Tipo sanguíneo", sangue) {
                sangue = it
            }

            Campo("Alergias", alergias) {
                alergias = it
            }

            Campo("Medicamentos em uso", medicamentos) {
                medicamentos = it
            }

            Campo("Condições importantes", condicoes) {
                condicoes = it
            }
        }

        CardSecao("Contato de confiança") {

            Campo("Nome do contato", contato) {
                contato = it
            }

            Campo("Telefone do contato", telefone) {
                telefone = it
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrincipal
            )
        ) {
            Text(
                text = "Salvar informações",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Essas informações serão conectadas ao banco de dados na próxima etapa.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }
}


// ============================================================
// LOCALIZAÇÃO
// ============================================================

@Composable
fun LocalizacaoScreen(
    onVoltar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Localização",
            subtitulo = "Veja onde você está.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(26.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    VerdeClaro,
                    RoundedCornerShape(18.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        Verde,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Localização pronta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Text(
                    text = "O GPS será conectado em breve.",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulClaro
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_localizacao
                        ),
                        contentDescription = "Localização",
                        modifier = Modifier.size(76.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Sua localização",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "O Google Maps será exibido aqui.",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        CardSecao("Informações da localização") {

            LinhaInformacao(
                "Endereço",
                "Ainda não disponível"
            )

            LinhaInformacao(
                "Última atualização",
                "Ainda não disponível"
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrincipal
            )
        ) {
            Text(
                "Atualizar localização",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text(
                "Compartilhar localização",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Sua localização poderá ser compartilhada com uma pessoa de confiança cadastrada no SeniorLink.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }
}


// ============================================================
// LEMBRETES
// ============================================================

@Composable
fun LembretesScreen(
    onVoltar: () -> Unit,
    onNovoLembrete: () -> Unit = {}
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var horario by remember {
        mutableStateOf("")
    }

    var lembretes by remember {
        mutableStateOf(listOf<String>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Lembretes",
            subtitulo = "Organize os horários importantes do seu dia.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(26.dp))

        Button(
            onClick = onNovoLembrete,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrincipal
            )
        ) {
            Text(
                "Adicionar novo lembrete",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Adicionar rapidamente",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("O que precisa lembrar?")
                    },
                    placeholder = {
                        Text("Ex.: Tomar medicamento")
                    }
                )

                OutlinedTextField(
                    value = horario,
                    onValueChange = {
                        horario = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Horário")
                    },
                    placeholder = {
                        Text("Ex.: 08:00")
                    }
                )

                Button(
                    onClick = {

                        if (titulo.isNotBlank()) {

                            lembretes =
                                lembretes +
                                        if (horario.isBlank()) {
                                            titulo
                                        } else {
                                            "$horario — $titulo"
                                        }

                            titulo = ""
                            horario = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Verde
                    )
                ) {
                    Text(
                        "Adicionar lembrete",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Meus lembretes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulEscuro
        )

        Spacer(Modifier.height(12.dp))

        if (lembretes.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LaranjaClaro
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_lembretes
                        ),
                        contentDescription = "Lembretes",
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Nenhum lembrete",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "Adicione um lembrete importante para o seu dia.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {

            lembretes.forEachIndexed { index, item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Branco
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    AzulClaro,
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(
                                    R.drawable.ic_lembretes
                                ),
                                contentDescription =
                                    "Lembrete",
                                modifier = Modifier.size(34.dp),
                                contentScale =
                                    ContentScale.Fit
                            )
                        }

                        Spacer(
                            Modifier.width(14.dp)
                        )

                        Text(
                            text = item,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = Texto
                        )

                        TextButton(
                            onClick = {
                                lembretes =
                                    lembretes.filterIndexed {
                                            i, _ ->
                                        i != index
                                    }
                            }
                        ) {
                            Text(
                                "Excluir",
                                color = Vermelho
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "Os lembretes serão conectados ao banco de dados e às notificações do celular.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }
}


// ============================================================
// APRENDER
// ============================================================

@Composable
fun AprenderScreen(
    onVoltar: () -> Unit
) {

    var aberto by remember {
        mutableStateOf(-1)
    }

    val tutoriais = listOf(
        "Aumentar o tamanho das letras" to
                "Abra Configurações, procure por Tela e ajuste o tamanho da fonte.",

        "Fazer uma ligação" to
                "Abra o aplicativo Telefone, escolha um contato e toque no botão de ligar.",

        "Usar o Wi-Fi" to
                "Abra Configurações, entre em Wi-Fi e selecione uma rede conhecida.",

        "Enviar uma mensagem" to
                "Abra o aplicativo de mensagens, escolha um contato, escreva e toque em enviar."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Aprender",
            subtitulo = "Aprenda a usar o celular passo a passo.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(24.dp))

        tutoriais.forEachIndexed { index, tutorial ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        aberto =
                            if (aberto == index) {
                                -1
                            } else {
                                index
                            }
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Branco
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = tutorial.first,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    if (aberto == index) {

                        Spacer(Modifier.height(12.dp))

                        HorizontalDivider(
                            color = Borda
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = tutorial.second,
                            fontSize = 15.sp,
                            lineHeight = 23.sp,
                            color = Texto
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                aberto = -1
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Fechar tutorial")
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(18.dp))

        RodapeTela()
    }
}


// ============================================================
// JOGOS
// ============================================================

@Composable
fun JogosScreen(
    onVoltar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Jogos",
            subtitulo = "Exercite a mente de forma simples e divertida.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulClaro
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                Branco,
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Image(
                            painter = painterResource(
                                R.drawable.ic_jogos
                            ),
                            contentDescription = "Jogos",
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Caça-palavras",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Encontre palavras e exercite sua atenção.",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = TextoSecundario
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        // Próxima etapa:
                        // abrir o jogo de caça-palavras.
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrincipal
                    )
                ) {

                    Text(
                        text = "Jogar agora",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Outros jogos",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text =
                        "Em breve teremos mais atividades para memória, atenção e raciocínio.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = TextoSecundario
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "O objetivo dos jogos é tornar o uso do SeniorLink mais agradável e estimular atividades simples de atenção.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }
}


// ============================================================
// EMERGÊNCIA
// ============================================================

@Composable
fun EmergenciaScreen(
    onVoltar: () -> Unit
) {

    var confirmar by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Emergência",
            subtitulo = "Peça ajuda quando precisar.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = VermelhoClaro
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .background(
                            Branco,
                            RoundedCornerShape(28.dp)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_emergencia
                        ),
                        contentDescription =
                            "Emergência",
                        modifier = Modifier.size(68.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Precisa de ajuda?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Vermelho,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text =
                        "Toque no botão abaixo para pedir ajuda a uma pessoa de confiança.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Texto,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                Button(
                    onClick = {
                        confirmar = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Vermelho
                    )
                ) {

                    Text(
                        text = "PEDIR AJUDA",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Pessoa de confiança",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text =
                        "O SeniorLink poderá avisar uma pessoa cadastrada quando você pedir ajuda.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(17.dp)
                ) {

                    Text(
                        text = "Configurar pessoa de confiança",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = AzulClaro
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        R.drawable.ic_localizacao
                    ),
                    contentDescription =
                        "Localização",
                    modifier = Modifier.size(44.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Sua localização",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text =
                            "Poderá ser enviada junto com o pedido de ajuda.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextoSecundario
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text =
                "O acionamento real de chamadas, alertas e localização será conectado aos serviços do SeniorLink nas próximas etapas.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }

    if (confirmar) {

        AlertDialog(
            onDismissRequest = {
                confirmar = false
            },

            title = {
                Text(
                    text = "Pedir ajuda?",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Vermelho
                )
            },

            text = {
                Text(
                    text =
                        "Você está pedindo ajuda. " +
                                "Quando os serviços estiverem conectados, " +
                                "o SeniorLink poderá avisar seu contato de confiança."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        confirmar = false
                    }
                ) {
                    Text(
                        "Confirmar",
                        color = Vermelho,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        confirmar = false
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = AzulPrincipal
                    )
                }
            }
        )
    }
}


// ============================================================
// CABEÇALHO
// ============================================================

@Composable
private fun CabecalhoTela(
    titulo: String,
    subtitulo: String,
    onVoltar: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Branco,
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    onVoltar()
                },
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(
                    R.drawable.ic_voltar
                ),
                contentDescription = "Voltar",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitulo,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                color = TextoSecundario
            )
        }
    }
}


// ============================================================
// CARD DE SEÇÃO
// ============================================================

@Composable
private fun CardSecao(
    titulo: String,
    conteudo: @Composable () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = titulo,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            conteudo()
        }
    }
}


// ============================================================
// CAMPO
// ============================================================

@Composable
private fun Campo(
    titulo: String,
    valor: String,
    aoAlterar: (String) -> Unit
) {

    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = {
            Text(titulo)
        }
    )
}


// ============================================================
// INFORMAÇÃO
// ============================================================

@Composable
private fun LinhaInformacao(
    titulo: String,
    valor: String
) {

    Column {

        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextoSecundario
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = valor,
            fontSize = 16.sp,
            color = Texto
        )

        Spacer(Modifier.height(10.dp))
    }
}


// ============================================================
// FOOTER
// ============================================================

@Composable
private fun RodapeTela() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalDivider(
            color = Borda,
            thickness = 1.dp
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "SeniorLink",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrincipal
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = "Cuidado, autonomia e conexão",
            fontSize = 11.sp,
            color = TextoSecundario
        )

        Spacer(Modifier.height(10.dp))
    }
}
