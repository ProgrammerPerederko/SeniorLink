
package com.seniorlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF1764A5)
private val AzulClaro = Color(0xFFE5F2FC)
private val Verde = Color(0xFF079B91)
private val VerdeClaro = Color(0xFFE5F6F3)
private val Laranja = Color(0xFFF39A22)
private val LaranjaClaro = Color(0xFFFFF2DF)
private val Vermelho = Color(0xFFE94B4B)
private val Fundo = Color(0xFFF7F9FC)
private val Texto = Color(0xFF18334D)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Fundo,
        bottomBar = {
            BarraInferior(onNavigate)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            // Cabeçalho
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Olá, Maria! 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )

                    Text(
                        text = "Que bom te ver por aqui!",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            AzulClaro,
                            RoundedCornerShape(50)
                        )
                        .clickable {
                            onNavigate("perfil")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 22.sp
                    )
                }
            }

            // Cartões principais
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CartaoFuncao(
                    titulo = "Meu perfil",
                    descricao = "Seus dados importantes",
                    emoji = "👤",
                    fundo = AzulClaro,
                    cor = Azul,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate("perfil")
                }

                CartaoFuncao(
                    titulo = "Localização",
                    descricao = "Ver sua localização",
                    emoji = "📍",
                    fundo = VerdeClaro,
                    cor = Verde,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate("localizacao")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CartaoFuncao(
                    titulo = "Lembretes",
                    descricao = "Não esqueça do importante",
                    emoji = "🔔",
                    fundo = LaranjaClaro,
                    cor = Laranja,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate("lembretes")
                }

                CartaoFuncao(
                    titulo = "Aprender",
                    descricao = "Tutoriais e dicas",
                    emoji = "🧩",
                    fundo = AzulClaro,
                    cor = Azul,
                    modifier = Modifier.weight(1f)
                ) {
                    onNavigate("aprender")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Emergência
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFFFE9E9),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onNavigate("emergencia")
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            Vermelho,
                            RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🆘",
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Emergência",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Vermelho
                    )

                    Text(
                        text = "Toque para acionar ajuda",
                        fontSize = 13.sp,
                        color = Color(0xFF875050)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = { onNavigate("configuracoes") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⚙ Configurações", color = Azul)
            }

            // Espaço para conteúdos futuros
            Text(
                text = "Cuidado, autonomia e conexão",
                fontSize = 13.sp,
                color = Color(0xFF8291A3)
            )
        }
    }
}

@Composable
private fun CartaoFuncao(
    titulo: String,
    descricao: String,
    emoji: String,
    fundo: Color,
    cor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(126.dp)
            .background(
                fundo,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = emoji,
            fontSize = 25.sp
        )

        Column {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = cor
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = descricao,
                fontSize = 11.sp,
                color = Texto,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun BarraInferior(
    onNavigate: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemNavegacao(
                emoji = "⌂",
                titulo = "Início",
                selecionado = true
            ) {
                onNavigate("inicio")
            }

            ItemNavegacao(
                emoji = "📍",
                titulo = "Localização"
            ) {
                onNavigate("localizacao")
            }

            ItemNavegacao(
                emoji = "🔔",
                titulo = "Lembretes"
            ) {
                onNavigate("lembretes")
            }

            ItemNavegacao(
                emoji = "👤",
                titulo = "Perfil"
            ) {
                onNavigate("perfil")
            }
        }
    }
}

@Composable
private fun ItemNavegacao(
    emoji: String,
    titulo: String,
    selecionado: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            fontSize = 21.sp,
            color = if (selecionado) Azul else Color.Gray
        )

        Text(
            text = titulo,
            fontSize = 10.sp,
            fontWeight = if (selecionado) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (selecionado) Azul else Color.Gray
        )
    }
}