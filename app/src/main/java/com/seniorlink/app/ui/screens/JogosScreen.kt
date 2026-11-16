package com.seniorlink.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.firebase.auth.FirebaseAuth
import com.seniorlink.app.R
import com.seniorlink.app.data.FirestoreRepository
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================
// CORES DO SENIORLINK
// ============================================================

private val AzulPrincipal = Color(0xFF06466B)
private val AzulEscuro = Color(0xFF123B63)
private val AzulClaro = Color(0xFFEAF4FC)
private val Verde = Color(0xFF159C8C)
private val VerdeClaro = Color(0xFFE6F7F4)
private val Ouro = Color(0xFFF59E0B)
private val OuroClaro = Color(0xFFFFF8E7)
private val Roxo = Color(0xFF6D28D9)
private val RoxoClaro = Color(0xFFF3E8FF)
private val Fundo = Color(0xFFF7F9FC)
private val Branco = Color.White
private val Texto = Color(0xFF18334D)
private val TextoSecundario = Color(0xFF66788A)
private val Borda = Color(0xFFD7E3E8)

// ============================================================
// MODELO DO RANKING
// ============================================================

private data class JogadorRanking(
    val nome: String,
    val avatar: String,
    val pontos: Int,
    val isUsuarioAtual: Boolean = false
)

// ============================================================
// TELA PRINCIPAL DE JOGOS + PLACAR (CONECTADA AO FIRESTORE)
// ============================================================

@Composable
fun JogosScreen(
    onVoltar: () -> Unit
) {
    var telaSelecionada by remember { mutableStateOf<String?>(null) }

    val firestoreRepo = remember { FirestoreRepository() }
    val coroutineScope = rememberCoroutineScope()
    val rankingNuvem by firestoreRepo.observarRanking().collectAsState(initial = emptyList())
    val uidAtual = remember { FirebaseAuth.getInstance().currentUser?.uid ?: "idoso_demo" }

    val listaRanking = remember(rankingNuvem, uidAtual) {
        val base = listOf(
            JogadorRanking("Dona Helena", "👵", 480),
            JogadorRanking("Sr. Antônio", "👴", 410),
            JogadorRanking("Dona Carmem", "🌸", 290),
            JogadorRanking("Sr. José", "🎩", 240)
        )
        val reais = rankingNuvem.map {
            JogadorRanking(
                nome = if (it.uid == uidAtual) "Você (${it.nome})" else it.nome,
                avatar = it.avatar,
                pontos = it.pontos,
                isUsuarioAtual = it.uid == uidAtual
            )
        }
        val temUsuario = reais.any { it.isUsuarioAtual }
        val listaCompleta = if (temUsuario) {
            reais + base
        } else {
            reais + base + JogadorRanking("Você (Maria)", "🌟", 0, isUsuarioAtual = true)
        }
        listaCompleta.sortedByDescending { it.pontos }
    }

    val pontosUsuario = listaRanking.firstOrNull { it.isUsuarioAtual }?.pontos ?: 0
    val posicaoUsuario = listaRanking.indexOfFirst { it.isUsuarioAtual } + 1

    val registrarPontos: (Int) -> Unit = { pts ->
        coroutineScope.launch {
            firestoreRepo.adicionarPontos(pts)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(22.dp)
    ) {
        // CABEÇALHO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_voltar),
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        if (telaSelecionada != null) {
                            telaSelecionada = null
                        } else {
                            onVoltar()
                        }
                    },
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = when (telaSelecionada) {
                    "ranking" -> "Placar dos Amigos"
                    "memoria" -> "Exercitar a Memória"
                    "palavras" -> "Cruzadinha de Palavras"
                    "matematica" -> "Continhas do Dia"
                    "ditados" -> "Ditados Populares"
                    "intruso" -> "Achar o Intruso"
                    else -> "Exercícios para a Mente"
                },
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        when (telaSelecionada) {
            null -> MenuJogosHumano(
                pontosUsuario = pontosUsuario,
                posicaoUsuario = posicaoUsuario,
                onAbrirRanking = { telaSelecionada = "ranking" },
                onSelecionarJogo = { telaSelecionada = it }
            )
            "ranking" -> RankingView(
                jogadores = listaRanking,
                onVoltarJogos = { telaSelecionada = null }
            )
            "memoria" -> JogoDaMemoriaView(
                onGanharPontos = registrarPontos
            )
            "palavras" -> JogoDePalavrasView(
                onGanharPontos = registrarPontos
            )
            "matematica" -> JogoDeMatematicaView(
                onGanharPontos = registrarPontos
            )
            "ditados" -> JogoDitadosView(
                onGanharPontos = registrarPontos
            )
            "intruso" -> JogoIntrusoView(
                onGanharPontos = registrarPontos
            )
        }
    }
}

// ============================================================
// MENU PRINCIPAL DE JOGOS + BANNER DE CONQUISTAS (HUMANIZADO)
// ============================================================

@Composable
private fun MenuJogosHumano(
    pontosUsuario: Int,
    posicaoUsuario: Int,
    onAbrirRanking: () -> Unit,
    onSelecionarJogo: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAbrirRanking() },
        shape = RoundedCornerShape(22.dp),
        color = OuroClaro,
        border = BorderStroke(1.5.dp, Ouro)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Branco, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⭐", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Suas Conquistas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$pontosUsuario estrelas • ${posicaoUsuario}º lugar no placar",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulEscuro
                )
            }

            Text(
                text = "Ver",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "O que você gostaria de fazer agora?",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AzulEscuro
    )

    Spacer(modifier = Modifier.height(14.dp))

    CardOpcaoJogoHumano(
        emoji = "🧠",
        titulo = "Exercitar a Memória",
        subtitulo = "Encontre os pares de figuras escondidas",
        onClick = { onSelecionarJogo("memoria") }
    )

    Spacer(modifier = Modifier.height(12.dp))

    CardOpcaoJogoHumano(
        emoji = "💬",
        titulo = "Ditados Populares",
        subtitulo = "Complete as frases que todo mundo conhece",
        onClick = { onSelecionarJogo("ditados") }
    )

    Spacer(modifier = Modifier.height(12.dp))

    CardOpcaoJogoHumano(
        emoji = "🔍",
        titulo = "Achar o Intruso",
        subtitulo = "Descubra qual figura não combina com as outras",
        onClick = { onSelecionarJogo("intruso") }
    )

    Spacer(modifier = Modifier.height(12.dp))

    CardOpcaoJogoHumano(
        emoji = "🔤",
        titulo = "Cruzadinha de Palavras",
        subtitulo = "Leia a dica e monte a palavra correta",
        onClick = { onSelecionarJogo("palavras") }
    )

    Spacer(modifier = Modifier.height(12.dp))

    CardOpcaoJogoHumano(
        emoji = "🔢",
        titulo = "Continhas do Dia",
        subtitulo = "Resolva somas leves para manter a mente ativa",
        onClick = { onSelecionarJogo("matematica") }
    )
}

@Composable
private fun CardOpcaoJogoHumano(
    emoji: String,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Branco,
        border = BorderStroke(1.dp, Borda),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
            }

            Text(
                text = "➔",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrincipal
            )
        }
    }
}

// ============================================================
// TELA DE PLACAR / RANKING ENTRE OS AMIGOS
// ============================================================

@Composable
private fun RankingView(
    jogadores: List<JogadorRanking>,
    onVoltarJogos: () -> Unit
) {
    Text(
        text = "Jogue mais partidas para subir no placar e acumular estrelas!",
        fontSize = 16.sp,
        color = TextoSecundario,
        lineHeight = 22.sp
    )

    Spacer(modifier = Modifier.height(20.dp))

    jogadores.forEachIndexed { index, jogador ->
        val posicao = index + 1
        val medalha = when (posicao) {
            1 -> "🥇"
            2 -> "🥈"
            3 -> "🥉"
            else -> "${posicao}º"
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(20.dp),
            color = if (jogador.isUsuarioAtual) VerdeClaro else Branco,
            border = BorderStroke(
                width = if (jogador.isUsuarioAtual) 2.dp else 1.dp,
                color = if (jogador.isUsuarioAtual) Verde else Borda
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = medalha,
                        fontSize = if (posicao <= 3) 28.sp else 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulEscuro
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(AzulClaro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = jogador.avatar, fontSize = 26.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = jogador.nome,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )
                    if (jogador.isUsuarioAtual) {
                        Text(
                            text = "Sua pontuação atual",
                            fontSize = 13.sp,
                            color = Verde,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (posicao == 1) OuroClaro else AzulClaro
                ) {
                    Text(
                        text = "${jogador.pontos} ⭐",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (posicao == 1) Color(0xFFB45309) else AzulPrincipal
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onVoltarJogos,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
    ) {
        Text("Voltar para os Jogos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

// ============================================================
// JOGO 1: JOGO DA MEMÓRIA
// ============================================================

private data class CartaMemoria(
    val id: Int,
    val emoji: String,
    val virada: Boolean = false,
    val combinada: Boolean = false
)

@Composable
private fun JogoDaMemoriaView(
    onGanharPontos: (Int) -> Unit
) {
    val emojisBase = remember { listOf("🌻", "🍎", "🐶", "☕", "🏡", "🐦") }

    fun criarBaralho(): List<CartaMemoria> {
        return (emojisBase + emojisBase)
            .shuffled()
            .mapIndexed { index, emoji -> CartaMemoria(id = index, emoji = emoji) }
    }

    var cartas by remember { mutableStateOf(criarBaralho()) }
    var selecionadas by remember { mutableStateOf<List<Int>>(emptyList()) }
    var tentativas by remember { mutableIntStateOf(0) }
    var pontuouPartida by remember { mutableStateOf(false) }

    val venceu = cartas.all { it.combinada }

    LaunchedEffect(venceu) {
        if (venceu && !pontuouPartida) {
            pontuouPartida = true
            onGanharPontos(100)
        }
    }

    LaunchedEffect(selecionadas) {
        if (selecionadas.size == 2) {
            delay(850.milliseconds)
            val primeira = cartas[selecionadas[0]]
            val segunda = cartas[selecionadas[1]]

            cartas = if (primeira.emoji == segunda.emoji) {
                cartas.mapIndexed { idx, carta ->
                    if (idx in selecionadas) carta.copy(combinada = true) else carta
                }
            } else {
                cartas.mapIndexed { idx, carta ->
                    if (idx in selecionadas) carta.copy(virada = false) else carta
                }
            }
            selecionadas = emptyList()
        }
    }

    Text(
        text = "Toque em duas cartas para encontrar os pares iguais.",
        fontSize = 16.sp,
        color = TextoSecundario
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Tentativas: $tentativas",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = AzulPrincipal
    )

    Spacer(modifier = Modifier.height(16.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        cartas.chunked(3).forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                linha.forEach { carta ->
                    val indexReal = cartas.indexOf(carta)
                    val visivel = carta.virada || carta.combinada

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable(enabled = !visivel && selecionadas.size < 2) {
                                cartas = cartas.mapIndexed { idx, c ->
                                    if (idx == indexReal) c.copy(virada = true) else c
                                }
                                val novas = selecionadas + indexReal
                                selecionadas = novas
                                if (novas.size == 2) tentativas++
                            },
                        shape = RoundedCornerShape(18.dp),
                        color = when {
                            carta.combinada -> VerdeClaro
                            visivel -> Branco
                            else -> AzulPrincipal
                        },
                        border = BorderStroke(2.dp, if (carta.combinada) Verde else Borda)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (visivel) carta.emoji else "?",
                                fontSize = if (visivel) 38.sp else 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (visivel) Texto else Branco
                            )
                        }
                    }
                }
            }
        }
    }

    if (venceu) {
        Spacer(modifier = Modifier.height(22.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = VerdeClaro,
            border = BorderStroke(1.5.dp, Verde)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎉 Parabéns! +100 estrelas!", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Verde)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Você encontrou todos os pares em $tentativas tentativas!",
                    fontSize = 16.sp,
                    color = Texto,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        cartas = criarBaralho()
                        selecionadas = emptyList()
                        tentativas = 0
                        pontuouPartida = false
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Verde)
                ) {
                    Text("Jogar Novamente", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ============================================================
// JOGO 2: COMPLETE O DITADO POPULAR
// ============================================================

private data class DesafioDitado(
    val inicio: String,
    val respostaCerta: String,
    val opcoes: List<String>
)

@Composable
private fun JogoDitadosView(
    onGanharPontos: (Int) -> Unit
) {
    val ditados = remember {
        listOf(
            DesafioDitado("Água mole em pedra dura, tanto bate até que...", "Fura", listOf("Fura", "Seca", "Quebra")),
            DesafioDitado("De grão em grão, a galinha...", "Enche o papo", listOf("Enche o papo", "Aprende a voar", "Canta cedo")),
            DesafioDitado("Quem canta seus males...", "Espanta", listOf("Espanta", "Esquece", "Acalma")),
            DesafioDitado("Mais vale um pássaro na mão do que...", "Dois voando", listOf("Dois voando", "Um na gaiola", "Três cantando")),
            DesafioDitado("Em terra de cego, quem tem um olho é...", "Rei", listOf("Rei", "Guia", "Mestre"))
        )
    }

    var indice by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }
    val atual = ditados[indice]

    Text(text = "Qual palavra completa o ditado popular?", fontSize = 16.sp, color = TextoSecundario)
    Spacer(modifier = Modifier.height(18.dp))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = RoxoClaro,
        border = BorderStroke(1.5.dp, Roxo)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("DITADO POPULAR:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Roxo)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "“${atual.inicio}”", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulEscuro, lineHeight = 30.sp)
        }
    }

    Spacer(modifier = Modifier.height(22.dp))

    atual.opcoes.forEach { opcao ->
        Button(
            onClick = {
                if (opcao == atual.respostaCerta) {
                    onGanharPontos(40)
                    feedback = "🎉 Acertou! +40 estrelas!"
                    indice = (indice + 1) % ditados.size
                } else {
                    feedback = "Quase lá! Tente outra opção."
                }
            },
            modifier = Modifier.fillMaxWidth().height(62.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Roxo)
        ) {
            Text(text = opcao, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Branco)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    feedback?.let { msg ->
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (msg.startsWith("🎉")) VerdeClaro else Color(0xFFFFF0F0)
        ) {
            Text(text = msg, modifier = Modifier.padding(16.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Texto, textAlign = TextAlign.Center)
        }
    }
}

// ============================================================
// JOGO 3: QUAL É O INTRUSO?
// ============================================================

private data class DesafioIntruso(
    val pergunta: String,
    val itens: List<Pair<String, String>>,
    val nomeIntruso: String
)

@Composable
private fun JogoIntrusoView(
    onGanharPontos: (Int) -> Unit
) {
    val desafios = remember {
        listOf(
            DesafioIntruso("Três são frutas e um não é. Qual é o intruso?", listOf("🍌" to "Banana", "🍎" to "Maçã", "🚗" to "Carro", "🍇" to "Uva"), "Carro"),
            DesafioIntruso("Três são animais e um não é. Qual é o intruso?", listOf("🐶" to "Cachorro", "🌻" to "Girassol", "🐱" to "Gato", "🐦" to "Pássaro"), "Girassol"),
            DesafioIntruso("Três são coisas de cozinha e um não é. Qual é o intruso?", listOf("☕" to "Xícara", "🥄" to "Colher", "👟" to "Tênis", "🍳" to "Panela"), "Tênis")
        )
    }

    var indice by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }
    val atual = desafios[indice]

    Text(text = atual.pergunta, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = AzulEscuro, lineHeight = 26.sp)
    Spacer(modifier = Modifier.height(20.dp))

    atual.itens.chunked(2).forEach { linha ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            linha.forEach { (emoji, nome) ->
                Surface(
                    modifier = Modifier.weight(1f).height(145.dp).clickable {
                        if (nome == atual.nomeIntruso) {
                            onGanharPontos(35)
                            feedback = "🎉 Muito bem! $nome era o intruso! (+35 estrelas)"
                            indice = (indice + 1) % desafios.size
                        } else {
                            feedback = "Esse faz parte do grupo! Procure o diferente."
                        }
                    },
                    shape = RoundedCornerShape(22.dp),
                    color = Branco,
                    border = BorderStroke(2.dp, Borda)
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = emoji, fontSize = 46.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = nome, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AzulEscuro)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
    }

    feedback?.let { msg ->
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (msg.startsWith("🎉")) VerdeClaro else Color(0xFFFFF0F0)
        ) {
            Text(text = msg, modifier = Modifier.padding(16.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Texto, textAlign = TextAlign.Center)
        }
    }
}

// ============================================================
// JOGO 4: ACERTE A PALAVRA
// ============================================================

private data class DesafioPalavra(val palavra: String, val dica: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun JogoDePalavrasView(onGanharPontos: (Int) -> Unit) {
    val desafios = remember {
        listOf(
            DesafioPalavra("CAFE", "Bebida quente muito tomada no café da manhã"),
            DesafioPalavra("JARDIM", "Lugar com flores, plantas e árvores"),
            DesafioPalavra("FAMILIA", "Pessoas que amamos e cuidam de nós"),
            DesafioPalavra("MUSICA", "Som agradável que gostamos de ouvir e cantar")
        )
    }

    var indiceAtual by remember { mutableIntStateOf(0) }
    var letrasDescobertas by remember { mutableStateOf(setOf<Char>()) }
    var pontuouPalavra by remember { mutableStateOf(false) }

    val desafioAtual = desafios[indiceAtual]
    val completou = desafioAtual.palavra.all { it in letrasDescobertas }
    val alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toList()

    LaunchedEffect(completou) {
        if (completou && !pontuouPalavra) {
            pontuouPalavra = true
            onGanharPontos(50)
        }
    }

    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = AzulClaro) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("DICA:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desafioAtual.dica, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, color = AzulEscuro)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
        desafioAtual.palavra.forEach { letra ->
            val revelada = letra in letrasDescobertas
            Surface(
                modifier = Modifier.size(width = 42.dp, height = 54.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (revelada) VerdeClaro else Branco,
                border = BorderStroke(2.dp, if (revelada) Verde else AzulPrincipal)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = if (revelada) letra.toString() else "", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AzulEscuro)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(26.dp))

    if (completou) {
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = VerdeClaro, border = BorderStroke(1.5.dp, Verde)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("👏 Muito bem! +50 estrelas!", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Verde)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Você acertou a palavra ${desafioAtual.palavra}!", fontSize = 17.sp, color = Texto)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        indiceAtual = (indiceAtual + 1) % desafios.size
                        letrasDescobertas = emptySet()
                        pontuouPalavra = false
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Verde)
                ) {
                    Text("Próxima Palavra", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        Text(text = "Toque nas letras abaixo:", fontSize = 15.sp, color = TextoSecundario)
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            alfabeto.forEach { letra ->
                val jaUsada = letra in letrasDescobertas
                val pertence = letra in desafioAtual.palavra

                Surface(
                    modifier = Modifier.size(48.dp).clickable(enabled = !jaUsada) {
                        letrasDescobertas = letrasDescobertas + letra
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        jaUsada && pertence -> Verde
                        jaUsada -> Color(0xFFE2E8F0)
                        else -> Branco
                    },
                    border = BorderStroke(1.dp, Borda)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = letra.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                jaUsada && pertence -> Branco
                                jaUsada -> TextoSecundario
                                else -> AzulEscuro
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// JOGO 5: CONTINHAS RÁPIDAS
// ============================================================

@Composable
private fun JogoDeMatematicaView(onGanharPontos: (Int) -> Unit) {
    var num1 by remember { mutableIntStateOf((3..15).random()) }
    var num2 by remember { mutableIntStateOf((2..12).random()) }
    var acertos by remember { mutableIntStateOf(0) }
    var mensagemFeedback by remember { mutableStateOf<String?>(null) }

    val respostaCorreta = num1 + num2
    val opcoes = remember(num1, num2) {
        setOf(
            respostaCorreta,
            respostaCorreta + (1..3).random(),
            (respostaCorreta - (1..2).random()).coerceAtLeast(1)
        ).toList().shuffled()
    }

    Text(text = "Acertos seguidos: $acertos ⭐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Verde)
    Spacer(modifier = Modifier.height(20.dp))

    Surface(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(24.dp),
        color = Branco,
        border = BorderStroke(2.dp, AzulPrincipal)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "$num1  +  $num2  =  ?", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = AzulEscuro)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    Text(text = "Toque na resposta certa:", fontSize = 17.sp, color = TextoSecundario)
    Spacer(modifier = Modifier.height(14.dp))

    opcoes.forEach { opcao ->
        Button(
            onClick = {
                if (opcao == respostaCorreta) {
                    acertos++
                    onGanharPontos(30)
                    mensagemFeedback = "🎉 Isso mesmo! $num1 + $num2 = $respostaCorreta (+30 estrelas)"
                    num1 = (3..18).random()
                    num2 = (2..15).random()
                } else {
                    mensagemFeedback = "Quase lá! Tente somar novamente."
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
        ) {
            Text(text = opcao.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Branco)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    mensagemFeedback?.let { feedback ->
        Spacer(modifier = Modifier.height(8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = if (feedback.startsWith("🎉")) VerdeClaro else Color(0xFFFFF0F0)) {
            Text(text = feedback, modifier = Modifier.padding(16.dp), fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Texto, textAlign = TextAlign.Center)
        }
    }
}