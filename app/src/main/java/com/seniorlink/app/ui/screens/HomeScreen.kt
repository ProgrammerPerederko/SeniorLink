package com.seniorlink.app.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.data.FirestoreRepository
import com.seniorlink.app.location.LocationRepository
import com.seniorlink.app.model.SeniorLinkPrefs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val AzulMarca = Color(0xFF1764A5)
private val AzulMarinho = Color(0xFF123B63)
private val TurquesaMarca = Color(0xFF079B91)
private val AzulSuave = Color(0xFFEAF3FB)
private val TurquesaSuave = Color(0xFFE6F6F4)
private val VermelhoAlerta = Color(0xFFD63636)
private val VermelhoSuave = Color(0xFFFDF2F2)
private val VermelhoBorda = Color(0xFFF5C6C6)
private val FundoTela = Color(0xFFF4F7FA)
private val Branco = Color.White
private val TextoPrincipal = Color(0xFF132D46)
private val TextoSuave = Color(0xFF5D7285)
private val BordaSutil = Color(0xFFDCE5EE)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }
    val locationRepo = remember { LocationRepository(context) }

    val escala = SeniorLinkPrefs.escalaFonte
    val altoContraste = SeniorLinkPrefs.altoContraste

    val fundoAtual = if (altoContraste) Color.Black else FundoTela
    val cardFundo = if (altoContraste) Color(0xFF1A1A1A) else Branco
    val textoTituloAtual = if (altoContraste) Color.Yellow else AzulMarinho
    val textoSecundarioAtual = if (altoContraste) Color.White else TextoSuave

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !altoContraste
            }
        }
    }

    val idosoId = remember {
        prefs.getString("id_idoso", null)
            ?: prefs.getString("idoso_id", null)
            ?: FirebaseAuth.getInstance().currentUser?.uid
            ?: ""
    }

    var nomeRealIdoso by remember { mutableStateOf("Carregando...") }
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
        }
    }

    var gpsNegado by remember { mutableStateOf(false) }

    fun iniciarEnvioLocalizacao() {
        if (idosoId.isBlank()) return
        locationRepo.iniciarAtualizacao { loc ->
            val dadosLoc = mapOf(
                "latitude" to loc.latitude,
                "longitude" to loc.longitude,
                "precisao" to loc.accuracy.toDouble(),
                "atualizadoEm" to System.currentTimeMillis()
            )
            FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(idosoId)
                .child("localizacao")
                .setValue(dadosLoc)
        }
    }

    val permissaoGpsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissoes ->
        val concedida = permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedida) {
            gpsNegado = false
            iniciarEnvioLocalizacao()
        } else {
            gpsNegado = true
        }
    }

    LaunchedEffect(idosoId) {
        val uid = idosoId.ifBlank { "idoso_teste" }
        SeniorLinkPrefs.carregarPerfilDoFirestore(uid)

        val temFina = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val temAproximada = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (temFina || temAproximada) {
            gpsNegado = false
            iniciarEnvioLocalizacao()
        } else {
            permissaoGpsLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    DisposableEffect(Unit) {
        onDispose { locationRepo.pararAtualizacao() }
    }

    val firestoreRepo = remember { FirestoreRepository() }
    val lembretes by firestoreRepo.observarLembretesPorIdoso(idosoId).collectAsState(initial = emptyList())

    val primeiroNome = remember(nomeRealIdoso) {
        nomeRealIdoso.split(" ").firstOrNull()?.takeIf { it.isNotBlank() } ?: "Usuário"
    }

    val saudacaoPeriodo = remember {
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hora) {
            in 5..11 -> "Bom dia"
            in 12..17 -> "Boa tarde"
            else -> "Boa noite"
        }
    }

    val dataHoje = remember {
        val formato = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.forLanguageTag("pt-BR"))
        formato.format(Calendar.getInstance().time).replaceFirstChar { it.uppercase() }
    }

    Scaffold(
        containerColor = fundoAtual,
        bottomBar = {
            FooterSeniorLink(cardFundo = cardFundo, textoSecundario = textoSecundarioAtual)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dataHoje,
                        fontSize = (14 * escala).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TurquesaMarca
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$saudacaoPeriodo, $primeiroNome",
                        fontSize = (26 * escala).sp,
                        fontWeight = FontWeight.Bold,
                        color = textoTituloAtual
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onNavigate("configuracoes") },
                    shape = CircleShape,
                    color = cardFundo,
                    border = BorderStroke(1.dp, BordaSutil),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⚙️", fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                AvatarIniciaisIdoso(
                    nome = nomeRealIdoso,
                    altoContraste = altoContraste,
                    onClick = { onNavigate("perfil") }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "O que você deseja acessar agora?",
                fontSize = (16 * escala).sp,
                color = textoSecundarioAtual
            )

            if (gpsNegado) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF4E5),
                    border = BorderStroke(1.5.dp, Color(0xFFB25E09))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚠️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Localização desativada",
                                fontWeight = FontWeight.Bold,
                                fontSize = (15 * escala).sp,
                                color = Color(0xFFB25E09)
                            )
                            Text(
                                text = "Toque aqui para liberar o GPS nas permissões.",
                                fontSize = (13 * escala).sp,
                                color = TextoPrincipal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CardAgendaHoje(
                proximoLembrete = lembretes.firstOrNull()?.let {
                    if (it.horario.isNotBlank()) "${it.horario} — ${it.titulo}" else it.titulo
                },
                totalLembretes = lembretes.size,
                escala = escala,
                cardFundo = cardFundo,
                textoTitulo = textoTituloAtual,
                textoSecundario = textoSecundarioAtual,
                onClick = { onNavigate("lembretes") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CardMenuPrincipal(
                    titulo = "Lembretes",
                    subtitulo = "Seus horários",
                    iconeEmoji = "⏰",
                    fundoIcone = AzulSuave,
                    escala = escala,
                    cardFundo = cardFundo,
                    textoTitulo = textoTituloAtual,
                    textoSecundario = textoSecundarioAtual,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate("lembretes") }
                )

                CardMenuPrincipal(
                    titulo = "Contatos",
                    subtitulo = "Ligar para família",
                    iconeEmoji = "📞",
                    fundoIcone = TurquesaSuave,
                    escala = escala,
                    cardFundo = cardFundo,
                    textoTitulo = textoTituloAtual,
                    textoSecundario = textoSecundarioAtual,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate("contatos") }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            CardJogosIdentidade(
                escala = escala,
                onClick = { onNavigate("jogos") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardEmergencia(
                escala = escala,
                onClick = { onNavigate("emergencia") }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AvatarIniciaisIdoso(
    nome: String,
    altoContraste: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(52.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = if (altoContraste) Color.Yellow else AzulMarca,
        border = BorderStroke(2.dp, Branco),
        shadowElevation = 3.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "👤", fontSize = 24.sp)
        }
    }
}

@Composable
private fun CardAgendaHoje(
    proximoLembrete: String?,
    totalLembretes: Int,
    escala: Float,
    cardFundo: Color,
    textoTitulo: Color,
    textoSecundario: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = cardFundo,
        border = BorderStroke(1.dp, BordaSutil),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(AzulSuave, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📅", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (proximoLembrete != null) "PRÓXIMO LEMBRETE" else "AGENDA DE HOJE",
                    fontSize = (11 * escala).sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = AzulMarca
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = proximoLembrete ?: "Nenhum horário salvo",
                    fontSize = (17 * escala).sp,
                    fontWeight = FontWeight.Bold,
                    color = textoTitulo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (totalLembretes > 1) "Mais ${totalLembretes - 1} lembrete(s) na agenda" else "Toque para ver ou adicionar horários",
                    fontSize = (13 * escala).sp,
                    color = textoSecundario
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "›", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulMarca)
        }
    }
}

@Composable
private fun CardMenuPrincipal(
    titulo: String,
    subtitulo: String,
    iconeEmoji: String,
    fundoIcone: Color,
    escala: Float,
    cardFundo: Color,
    textoTitulo: Color,
    textoSecundario: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height((162 * escala.coerceAtMost(1.25f)).dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        color = cardFundo,
        border = BorderStroke(1.dp, BordaSutil),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(fundoIcone, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconeEmoji, fontSize = 28.sp)
            }

            Column {
                Text(
                    text = titulo,
                    fontSize = (20 * escala).sp,
                    fontWeight = FontWeight.Bold,
                    color = textoTitulo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitulo,
                    fontSize = (14 * escala).sp,
                    color = textoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CardJogosIdentidade(escala: Float, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.horizontalGradient(colors = listOf(AzulMarca, TurquesaMarca)))
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Branco, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎮", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Jogos e Ranking", fontSize = (20 * escala).sp, fontWeight = FontWeight.Bold, color = Branco)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Exercite a memória e acompanhe seus pontos", fontSize = (14 * escala).sp, color = Branco.copy(alpha = 0.9f))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "›", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Branco)
        }
    }
}

@Composable
private fun CardEmergencia(escala: Float, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        color = VermelhoSuave,
        border = BorderStroke(1.5.dp, VermelhoBorda)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Branco, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🚨", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Preciso de ajuda", fontSize = (20 * escala).sp, fontWeight = FontWeight.Bold, color = VermelhoAlerta)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Acionar contato de emergência", fontSize = (14 * escala).sp, color = TextoPrincipal)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "›", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = VermelhoAlerta)
        }
    }
}

@Composable
private fun FooterSeniorLink(cardFundo: Color, textoSecundario: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardFundo)
            .navigationBarsPadding()
            .padding(top = 10.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = BordaSutil, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "SeniorLink", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AzulMarca, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Cuidado, autonomia e conexão", fontSize = 11.sp, color = textoSecundario, textAlign = TextAlign.Center)
    }
}