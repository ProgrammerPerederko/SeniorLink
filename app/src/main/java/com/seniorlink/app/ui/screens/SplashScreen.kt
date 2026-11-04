package com.seniorlink.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.seniorlink.app.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashScreen(
    onContinuar: () -> Unit
) {

    val azulMarinho = Color(0xFF123B63)
    val verdeAgua = Color(0xFF159C8C)
    val cinza = Color(0xFFB9D9E1)

    val paginas = listOf(

        OnboardingData(
            imagem = R.drawable.seniorlink_logo,
            titulo = "Cuidado hoje,\nmais liberdade amanhã",
            descricao = "O SeniorLink ajuda você a estar sempre por perto, com segurança e praticidade."
        ),

        OnboardingData(
            imagem = R.drawable.onboarding_cuidado,
            titulo = "Localização em\ntempo real",
            descricao = "Acompanhe a localização de quem você ama, com segurança e privacidade."
        ),

        OnboardingData(
            imagem = R.drawable.onboarding_localizacao,
            titulo = "Lembretes que\nfazem a diferença",
            descricao = "Medicamentos, compromissos e muito mais. Tudo no tempo certo."
        ),

        OnboardingData(
            imagem = R.drawable.seniorlink_logo,
            titulo = "Ajuda sempre\npor perto",
            descricao = "Botão de emergência e ligação rápida para seus contatos de confiança."
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            paginas.size
        }
    )

    /*
     * Quando o usuário chegar à última tela,
     * esperamos um pouco para ele visualizar
     * a tela e depois seguimos para o Login.
     */
    LaunchedEffect(pagerState.currentPage) {

        if (pagerState.currentPage == paginas.lastIndex) {

            delay(1500)

            onContinuar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pagina ->

            val item = paginas[pagina]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(
                            id = item.imagem
                        ),
                        contentDescription = item.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                androidx.compose.material3.Text(
                    text = item.titulo,
                    color = azulMarinho,
                    fontSize = 27.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                androidx.compose.material3.Text(
                    text = item.descricao,
                    color = Color(0xFF52677A),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        /*
         * Bolinhas de navegação.
         *
         * Elas acompanham automaticamente
         * a página atual do ViewPager.
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 30.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            paginas.indices.forEach { indice ->

                val ativo =
                    pagerState.currentPage == indice

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(
                            if (ativo) {
                                9.dp
                            } else {
                                7.dp
                            }
                        )
                        .background(
                            color = if (ativo) {
                                verdeAgua
                            } else {
                                cinza
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

data class OnboardingData(
    val imagem: Int,
    val titulo: String,
    val descricao: String
)