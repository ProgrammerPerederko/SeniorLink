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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private val azulPrincipal = Color(0xFF1764A5)
private val azulEscuro = Color(0xFF123B63)
private val azulClaro = Color(0xFFEAF4FC)

private val fundo = Color(0xFFF7F9FC)

private val fundoEmergencia = Color(0xFFFFF0F0)
private val vermelho = Color(0xFFE94B4B)

private val branco = Color.White

private val texto = Color(0xFF18334D)
private val textoSecundario = Color(0xFF66788A)

private val borda = Color(0xFFE1E8EF)


// ============================================================
// HOME DO IDOSO
// ============================================================

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {

    Scaffold(
        containerColor = fundo,

        bottomBar = {
            FooterSeniorLink()
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {

                // ==================================================
                // CABEÇALHO
                // ==================================================

                Text(
                    text = "Olá, Maria!",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = azulEscuro
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Como posso ajudar você?",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    color = textoSecundario
                )


                Spacer(
                    modifier = Modifier.height(30.dp)
                )


                // ==================================================
                // EMERGÊNCIA
                // ==================================================

                CardEmergencia(
                    onClick = {
                        onNavigate("emergencia")
                    }
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // ==================================================
                // PRIMEIRA LINHA
                // ==================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CardaoIdoso(
                        titulo = "Localização",
                        descricao = "Ver onde estou",
                        icone = R.drawable.ic_localizacao,
                        modifier = Modifier.weight(1f)
                    ) {
                        onNavigate("localizacao")
                    }

                    CardaoIdoso(
                        titulo = "Lembretes",
                        descricao = "Meus horários",
                        icone = R.drawable.ic_lembretes,
                        modifier = Modifier.weight(1f)
                    ) {
                        onNavigate("lembretes")
                    }
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // ==================================================
                // SEGUNDA LINHA
                // ==================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CardaoIdoso(
                        titulo = "Contatos",
                        descricao = "Pessoas de confiança",
                        icone = R.drawable.ic_contatos,
                        modifier = Modifier.weight(1f)
                    ) {
                        onNavigate("contatos")
                    }

                    CardaoIdoso(
                        titulo = "Jogos",
                        descricao = "Exercite a mente",
                        icone = R.drawable.ic_jogos,
                        modifier = Modifier.weight(1f)
                    ) {
                        onNavigate("jogos")
                    }
                }


                Spacer(
                    modifier = Modifier.height(24.dp)
                )


                // ==================================================
                // SEPARADOR
                // ==================================================

                HorizontalDivider(
                    color = borda,
                    thickness = 1.dp
                )


                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}


// ============================================================
// CARD DE EMERGÊNCIA
// ============================================================

@Composable
private fun CardEmergencia(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(94.dp)
            .background(
                color = fundoEmergencia,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        color = branco,
                        shape = RoundedCornerShape(17.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.ic_emergencia
                    ),
                    contentDescription = "Emergência",
                    modifier = Modifier.size(44.dp),
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
                    text = "Preciso de ajuda",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = vermelho
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Toque aqui em caso de emergência",
                    fontSize = 13.sp,
                    color = Color(0xFF8A5A5A)
                )
            }

            Text(
                text = "›",
                fontSize = 32.sp,
                color = vermelho
            )
        }
    }
}


// ============================================================
// CARD DO IDOSO
// ============================================================

@Composable
private fun CardaoIdoso(
    titulo: String,
    descricao: String,
    icone: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .height(154.dp)
            .background(
                color = azulClaro,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            }
            .padding(17.dp),

        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = branco,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(
                    id = icone
                ),
                contentDescription = titulo,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
        }


        Column {

            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = azulEscuro
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = descricao,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = texto
            )
        }
    }
}


// ============================================================
// FOOTER
// ============================================================

@Composable
private fun FooterSeniorLink() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = branco
            )
            .navigationBarsPadding()
            .padding(
                start = 22.dp,
                top = 10.dp,
                end = 22.dp,
                bottom = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalDivider(
            color = borda,
            thickness = 1.dp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "SeniorLink",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = azulPrincipal,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Cuidado, autonomia e conexão",
            fontSize = 11.sp,
            color = textoSecundario,
            textAlign = TextAlign.Center
        )
    }
}
