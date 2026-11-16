package com.seniorlink.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seniorlink.app.R
import kotlinx.coroutines.launch

private val AzulMarinho = Color(0xFF123B63)
private val TurquesaMarca = Color(0xFF079B91)
private val TextoSuave = Color(0xFF5D7285)

private data class PaginaOnboarding(
    val titulo: String,
    val descricao: String,
    val imagemRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashScreen(
    onContinuar: () -> Unit
) {
    val paginas = listOf(
        PaginaOnboarding(
            titulo = "Cuidado hoje,\nmais liberdade amanhã",
            descricao = "O SeniorLink ajuda você a estar sempre por perto, com segurança e praticidade.",
            imagemRes = R.drawable.seniorlink_logo
        ),
        PaginaOnboarding(
            titulo = "Cuidado e rotina\nsem complicação",
            descricao = "Acompanhe horários de medicamentos, lembretes e atividades diárias com facilidade.",
            imagemRes = R.drawable.onboarding_cuidado
        ),
        PaginaOnboarding(
            titulo = "Localização e apoio\nem tempo real",
            descricao = "Familiares e cuidadores acompanham tudo com tranquilidade e respeito à autonomia.",
            imagemRes = R.drawable.onboarding_localizacao
        ),
        PaginaOnboarding(
            titulo = "Aprendizado simples\nno seu ritmo",
            descricao = "Tutoriais práticos e claros para usar o celular com total confiança.",
            imagemRes = R.drawable.onboarding_aprendizado
        )
    )

    val pagerState = rememberPagerState(pageCount = { paginas.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp, vertical = 16.dp)
    ) {
        // Botão Pular discreto no topo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (pagerState.currentPage < paginas.lastIndex) {
                TextButton(onClick = onContinuar) {
                    Text(
                        text = "Pular",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoSuave
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        // Carrossel deslizável com as 4 telas originais
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { index ->
            val pagina = paginas[index]

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = pagina.imagemRes),
                        contentDescription = pagina.titulo,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = pagina.titulo,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    color = AzulMarinho
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pagina.descricao,
                    fontSize = 16.sp,
                    lineHeight = 23.sp,
                    color = TextoSuave
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Rodapé: 4 pontinhos + botão Próximo / Começar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(paginas.size) { index ->
                    val ativo = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .width(if (ativo) 24.dp else 10.dp)
                            .clip(CircleShape)
                            .background(if (ativo) TurquesaMarca else Color(0xFFD5E2E8))
                            .clickable {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            }
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < paginas.lastIndex) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onContinuar()
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TurquesaMarca),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage == paginas.lastIndex) "Começar" else "Próximo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}