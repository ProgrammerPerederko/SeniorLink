package com.seniorlink.app.ui.screens
import com.seniorlink.app.R
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

/*
 * ============================================================
 * CORES DO SENIORLINK
 * ============================================================
 */

private val AzulPrincipal = Color(0xFF1764A5)
private val AzulEscuro = Color(0xFF123B63)
private val AzulClaro = Color(0xFFEAF4FC)

private val Verde = Color(0xFF079B91)
private val VerdeClaro = Color(0xFFE8F7F5)

private val Vermelho = Color(0xFFE94B4B)
private val VermelhoClaro = Color(0xFFFFEEEE)

private val Fundo = Color(0xFFF7F9FC)
private val Texto = Color(0xFF18334D)
private val TextoSecundario = Color(0xFF66788A)
private val Borda = Color(0xFFE1E8EF)

/*
 * ============================================================
 * TELA DO RESPONSÁVEL
 * ============================================================
 */

@Composable
fun ResponsavelScreen(
    onVoltar: () -> Unit,
    onLocalizacao: () -> Unit,
    onFichaIdoso: () -> Unit,
    onLembretes: () -> Unit,
    onContatos: () -> Unit,
    onEmergencia: () -> Unit,
    onTreinamento: () -> Unit
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

                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    top = 40.dp,
                    end = 20.dp,
                    bottom = 28.dp
                ),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /*
                 * =================================================
                 * CABEÇALHO
                 * =================================================
                 */

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
                                text = "Área do responsável",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulEscuro
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                            Text(
                                text = "Acompanhe e cuide de quem você ama.",
                                fontSize = 14.sp,
                                color = TextoSecundario
                            )
                        }
                    }
                }

                /*
                 * =================================================
                 * SAUDAÇÃO
                 * =================================================
                 */

                item {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Olá, responsável!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Tenha acesso rápido às principais informações do idoso.",
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        color = TextoSecundario
                    )
                }

                /*
                 * =================================================
                 * PESSOA ACOMPANHADA
                 * =================================================
                 */

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Borda
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Row(
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
                                        text = "Acompanhamento ativo",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Verde
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(18.dp)
                            )

                            Divider(
                                color = Borda
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = "Status",
                                        fontSize = 12.sp,
                                        color = TextoSecundario
                                    )

                                    Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )

                                    Text(
                                        text = "Tudo certo",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Texto
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {

                                    Text(
                                        text = "Última atualização",
                                        fontSize = 12.sp,
                                        color = TextoSecundario
                                    )

                                    Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )

                                    Text(
                                        text = "Agora",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Texto
                                    )
                                }
                            }
                        }
                    }
                }

                /*
                 * =================================================
                 * TÍTULO DAS FUNÇÕES
                 * =================================================
                 */

                item {

                    Text(
                        text = "Acompanhamento",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )
                }

                /*
                 * =================================================
                 * LOCALIZAÇÃO / FICHA
                 * =================================================
                 */

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        AcaoResponsavelCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Localização",
                            descricao = "Veja onde o idoso está.",
                            iconRes = R.drawable.ic_localizacao,
                            corFundo = AzulClaro,
                            onClick = onLocalizacao
                        )

                        AcaoResponsavelCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Ficha do idoso",
                            descricao = "Dados importantes para cuidados.",
                            iconRes = R.drawable.icon_idoso,
                            corFundo = VerdeClaro,
                            onClick = onFichaIdoso
                        )
                    }
                }

                /*
                 * =================================================
                 * LEMBRETES / CONTATOS
                 * =================================================
                 */

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        AcaoResponsavelCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Lembretes",
                            descricao = "Acompanhe os lembretes.",
                            iconRes = R.drawable.ic_lembretes,
                            corFundo = AzulClaro,
                            onClick = onLembretes
                        )

                        AcaoResponsavelCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Contatos",
                            descricao = "Gerencie pessoas de confiança.",
                            iconRes = R.drawable.ic_contatos,
                            corFundo = VerdeClaro,
                            onClick = onContatos
                        )
                    }
                }

                /*
                 * =================================================
                 * EMERGÊNCIA
                 * =================================================
                 */

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
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 18.dp
                                ),

                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Color.White.copy(
                                            alpha = 0.18f
                                        )
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Image(
                                    painter = painterResource(
                                        id = R.drawable.ic_emergencia
                                    ),
                                    contentDescription = "Emergência",
                                    modifier = Modifier.size(38.dp)
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

                /*
                 * =================================================
                 * TREINAMENTO
                 * =================================================
                 */

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Borda
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(VerdeClaro),

                                    contentAlignment = Alignment.Center
                                ) {

                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.icon_responsavel
                                        ),
                                        contentDescription = "Responsável",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.width(14.dp)
                                )

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = "Treinamento do responsável",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulEscuro
                                    )

                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Text(
                                        text = "Aprenda a utilizar o SeniorLink e ajudar o idoso.",
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = TextoSecundario
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Button(
                                onClick = onTreinamento,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),

                                shape = RoundedCornerShape(15.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AzulPrincipal
                                )
                            ) {

                                Text(
                                    text = "ACESSAR TREINAMENTO",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                /*
                 * =================================================
                 * TEXTO FINAL
                 * =================================================
                 */

                item {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "O SeniorLink ajuda você a acompanhar o idoso com mais segurança, autonomia e tranquilidade.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = TextoSecundario
                    )
                }
            }

            /*
             * =====================================================
             * RODAPÉ
             * =====================================================
             */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),
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

/*
 * ============================================================
 * CARD DE AÇÃO
 * ============================================================
 */

@Composable
private fun AcaoResponsavelCard(
    modifier: Modifier = Modifier,
    titulo: String,
    descricao: String,
    iconRes: Int,
    corFundo: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(170.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Borda
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(corFundo),

                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(
                        id = iconRes
                    ),
                    contentDescription = titulo,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = titulo,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = descricao,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextoSecundario
            )
        }
    }
}