package com.seniorlink.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.seniorlink.app.R


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

                verticalArrangement = Arrangement.spacedBy(
                    16.dp
                )
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
                                .clip(
                                    RoundedCornerShape(14.dp)
                                )
                                .background(Color.White)
                                .clickable {
                                    onVoltar()
                                },

                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(
                                    id = R.drawable.ic_voltar
                                ),
                                contentDescription = "Voltar",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Área do cuidador",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulEscuro
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Text(
                                text = "Cuidados, rotina e segurança.",
                                fontSize = 14.sp,
                                color = TextoSecundario
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(
                                    RoundedCornerShape(14.dp)
                                )
                                .background(VerdeClaro),

                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(
                                    id = R.drawable.icon_cuidador
                                ),
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

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Olá, cuidador!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = "Acompanhe os principais cuidados do idoso durante o dia.",
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        color = TextoSecundario
                    )
                }


                // ====================================================
                // PESSOA ACOMPANHADA
                // ====================================================

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(22.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        border = BorderStroke(
                            1.dp,
                            Borda
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(
                                        RoundedCornerShape(18.dp)
                                    )
                                    .background(AzulClaro),

                                contentAlignment = Alignment.Center
                            ) {

                                Image(
                                    painter = painterResource(
                                        id = R.drawable.icon_idoso
                                    ),
                                    contentDescription = "Idoso",
                                    modifier = Modifier.size(46.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(14.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "Pessoa acompanhada",
                                    fontSize = 13.sp,
                                    color = TextoSecundario
                                )

                                Spacer(
                                    modifier = Modifier.height(3.dp)
                                )

                                Text(
                                    text = "Maria",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulEscuro
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "Cuidados em andamento",
                                    fontSize = 14.sp,
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
                        fontSize = 21.sp,
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
                            .clickable {
                                onEmergencia()
                            },

                        shape = RoundedCornerShape(22.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Vermelho
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(
                                        RoundedCornerShape(18.dp)
                                    )
                                    .background(
                                        Color.White.copy(
                                            alpha = 0.18f
                                        )
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Image(
                                    painter = painterResource(
                                        id = R.drawable.ic_emergencia
                                    ),
                                    contentDescription = "Emergência",
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(16.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "Emergência",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "Acesse rapidamente a função de ajuda.",
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = Color.White.copy(
                                        alpha = 0.92f
                                    )
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
                        onClick = onOrientacoes
                    )
                }


                // ====================================================
                // MENSAGEM
                // ====================================================

                item {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "O SeniorLink ajuda o cuidador a manter a rotina do idoso mais segura e organizada.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
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
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
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
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        border = BorderStroke(
            1.dp,
            Borda
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .background(fundoIcone),

                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(
                        id = iconRes
                    ),
                    contentDescription = titulo,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = descricao,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}