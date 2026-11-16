package com.seniorlink.app.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.R
import com.seniorlink.app.model.SeniorLinkPrefs

// ============================================================
// CORES DO SENIORLINK
// ============================================================

private val AzulPrincipal = Color(0xFF1764A5)
private val AzulEscuro = Color(0xFF123B63)
private val AzulClaro = Color(0xFFEAF4FC)

private val Verde = Color(0xFF079B91)
private val VerdeClaro = Color(0xFFE8F7F5)

private val Vermelho = Color(0xFFE94B4B)

private val Fundo = Color(0xFFF7F9FC)
private val Texto = Color(0xFF18334D)
private val TextoSecundario = Color(0xFF66788A)
private val Borda = Color(0xFFE1E8EF)


// ============================================================
// ÁREA DO CUIDADOR
// ============================================================

@Composable
fun CuidadorScreen(
    onVoltar: () -> Unit,
    onAgenda: () -> Unit,
    onLocalizacao: () -> Unit,
    onEmergencia: () -> Unit,
    onOrientacoes: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }
    val escala = SeniorLinkPrefs.escalaFonte

    val idosoId = remember {
        prefs.getString("id_idoso", null)
            ?: prefs.getString("idoso_id", null)
            ?: FirebaseAuth.getInstance().currentUser?.uid
            ?: ""
    }

    var nomeRealIdoso by remember { mutableStateOf("Carregando...") }

    // Carrega o nome real do idoso do Firebase Realtime Database
    LaunchedEffect(idosoId) {
        if (idosoId.isNotBlank()) {
            FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId).child("nome")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val nomeDb = snapshot.getValue(String::class.java)
                        if (!nomeDb.isNullOrBlank()) {
                            nomeRealIdoso = nomeDb
                        } else {
                            nomeRealIdoso = prefs.getString("nome_idoso", null) ?: "Usuário"
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        nomeRealIdoso = "Usuário"
                    }
                })
        } else {
            nomeRealIdoso = prefs.getString("nome_idoso", null) ?: "Usuário"
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Fundo
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 40.dp,
                    end = 20.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ====================================================
                // CABEÇALHO
                // ====================================================
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable { onVoltar() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_voltar),
                                contentDescription = "Voltar",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Área do cuidador",
                                fontSize = (22 * escala).sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulEscuro
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Cuidados, rotina e segurança.",
                                fontSize = (13 * escala).sp,
                                color = TextoSecundario
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VerdeClaro),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_cuidador),
                                contentDescription = "Cuidador",
                                modifier = Modifier.size(34.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                // ====================================================
                // SAUDAÇÃO
                // ====================================================
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Olá, cuidador!",
                        fontSize = (26 * escala).sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Acompanhe os principais cuidados do idoso durante o dia.",
                        fontSize = (15 * escala).sp,
                        lineHeight = (22 * escala).sp,
                        color = TextoSecundario
                    )
                }

                // ====================================================
                // PESSOA ACOMPANHADA (NOME REAL DINÂMICO)
                // ====================================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Borda),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(AzulClaro),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_idoso),
                                    contentDescription = "Idoso",
                                    modifier = Modifier.size(46.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Pessoa acompanhada",
                                    fontSize = (12 * escala).sp,
                                    color = TextoSecundario
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = nomeRealIdoso,
                                    fontSize = (20 * escala).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulEscuro
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Cuidados em andamento",
                                    fontSize = (13 * escala).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Verde
                                )
                            }
                        }
                    }
                }

                // ====================================================
                // TÍTULO
                // ====================================================
                item {
                    Text(
                        text = "Cuidados de hoje",
                        fontSize = (20 * escala).sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )
                }

                // ====================================================
                // AGENDA DE CUIDADOS
                // ====================================================
                item {
                    AcaoCuidadorCard(
                        titulo = "Agenda de cuidados",
                        descricao = "Veja os lembretes e tarefas do dia.",
                        iconRes = R.drawable.ic_lembretes,
                        fundoIcone = AzulClaro,
                        escala = escala,
                        onClick = onAgenda
                    )
                }

                // ====================================================
                // LOCALIZAÇÃO
                // ====================================================
                item {
                    AcaoCuidadorCard(
                        titulo = "Localização",
                        descricao = "Confira a localização do idoso.",
                        iconRes = R.drawable.ic_localizacao,
                        fundoIcone = AzulClaro,
                        escala = escala,
                        onClick = onLocalizacao
                    )
                }

                // ====================================================
                // EMERGÊNCIA
                // ====================================================
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEmergencia() },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Vermelho),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_emergencia),
                                    contentDescription = "Emergência",
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Emergência",
                                    fontSize = (19 * escala).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Acesse rapidamente a função de ajuda.",
                                    fontSize = (13 * escala).sp,
                                    lineHeight = (19 * escala).sp,
                                    color = Color.White.copy(alpha = 0.92f)
                                )
                            }
                        }
                    }
                }

                // ====================================================
                // ORIENTAÇÕES
                // ====================================================
                item {
                    AcaoCuidadorCard(
                        titulo = "Orientações",
                        descricao = "Aprenda a utilizar os recursos do SeniorLink.",
                        iconRes = R.drawable.icon_cuidador,
                        fundoIcone = VerdeClaro,
                        escala = escala,
                        onClick = onOrientacoes
                    )
                }

                // ====================================================
                // MENSAGEM
                // ====================================================
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "O SeniorLink ajuda o cuidador a manter a rotina do idoso mais segura e organizada.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = (13 * escala).sp,
                        lineHeight = (20 * escala).sp,
                        color = TextoSecundario
                    )
                }
            }

            // ====================================================
            // RODAPÉ
            // ====================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SeniorLink",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrincipal
                )
                Text(
                    text = "  •  Cuidado, autonomia e conexão",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}


// ============================================================
// CARD DO CUIDADOR
// ============================================================

@Composable
private fun AcaoCuidadorCard(
    titulo: String,
    descricao: String,
    iconRes: Int,
    fundoIcone: Color,
    escala: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Borda),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(fundoIcone),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = titulo,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo,
                    fontSize = (17 * escala).sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descricao,
                    fontSize = (13 * escala).sp,
                    lineHeight = (18 * escala).sp,
                    color = TextoSecundario
                )
            }
        }
    }
}