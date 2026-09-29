package com.seniorlink.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.seniorlink.app.R

@Composable
fun SplashScreen(
    onContinuar: () -> Unit
) {
    val azulMarinho = Color(0xFF123B63)
    val verdeAgua = Color(0xFF159C8C)
    val cinza = Color(0xFFB9D9E1)

    var paginaAtual by remember {
        mutableIntStateOf(0)
    }

    val totalPaginas = 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            )
    ) {

        // Área principal: logo do SeniorLink
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.seniorlink_logo
                ),
                contentDescription = "Logo do SeniorLink",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Navegação inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Botão Pular
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Pular",
                    color = azulMarinho,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable {
                            onContinuar()
                        }
                        .padding(8.dp)
                )
            }

            // Indicadores de página
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalPaginas) { indice ->

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(
                                if (paginaAtual == indice) {
                                    7.dp
                                } else {
                                    5.dp
                                }
                            )
                            .background(
                                color = if (paginaAtual == indice) {
                                    verdeAgua
                                } else {
                                    cinza
                                },
                                shape = CircleShape
                            )
                    )
                }
            }

            // Botão redondo para avançar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = verdeAgua,
                            shape = CircleShape
                        )
                        .clickable {

                            if (paginaAtual < totalPaginas - 1) {
                                paginaAtual++
                            } else {
                                onContinuar()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "→",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}