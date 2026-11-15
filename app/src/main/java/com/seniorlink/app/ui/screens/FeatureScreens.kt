package com.seniorlink.app.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.seniorlink.app.R
import com.seniorlink.app.alarm.LembreteScheduler
import com.seniorlink.app.data.FirestoreRepository
import com.seniorlink.app.data.LembreteFirestore
import com.seniorlink.app.location.LocationRepository
import com.seniorlink.app.model.SeniorLinkPrefs
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale


// ============================================================
// CORES
// ============================================================

private val AzulPrincipal = Color(0xFF1764A5)
private val AzulEscuro = Color(0xFF123B63)
private val AzulClaro = Color(0xFFEAF4FC)

private val Verde = Color(0xFF079B91)
private val VerdeClaro = Color(0xFFE8F7F5)

private val Vermelho = Color(0xFFE94B4B)
private val VermelhoClaro = Color(0xFFFFF0F0)

private val Fundo = Color(0xFFF7F9FC)
private val Branco = Color.White

private val Texto = Color(0xFF18334D)
private val TextoSecundario = Color(0xFF66788A)

private val Borda = Color(0xFFE1E8EF)


// ============================================================
// PERFIL DO IDOSO (INTEGRADO COM FIRESTORE E PREFERÊNCIAS)
// ============================================================

@Composable
fun PerfilScreen(
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val escala = SeniorLinkPrefs.escalaFonte
    val altoContraste = SeniorLinkPrefs.altoContraste

    val fundoAtual = if (altoContraste) Color.Black else Fundo
    val cardFundo = if (altoContraste) Color(0xFF1A1A1A) else Branco
    val tituloAtual = if (altoContraste) Color.Yellow else AzulEscuro
    val subtituloAtual = if (altoContraste) Color.White else TextoSecundario

    val perfilSalvo = SeniorLinkPrefs.perfilAtual

    var nome by remember(perfilSalvo.nome) { mutableStateOf(perfilSalvo.nome) }
    var idade by remember(perfilSalvo.idade) { mutableStateOf(perfilSalvo.idade) }
    var sangue by remember(perfilSalvo.tipoSanguineo) { mutableStateOf(perfilSalvo.tipoSanguineo) }
    var alergias by remember(perfilSalvo.alergias) { mutableStateOf(perfilSalvo.alergias) }
    var condicoes by remember(perfilSalvo.condicoesMedicas) { mutableStateOf(perfilSalvo.condicoesMedicas) }
    var contatoEmergencia by remember(perfilSalvo.contatoEmergencia) { mutableStateOf(perfilSalvo.contatoEmergencia) }
    var endereco by remember(perfilSalvo.endereco) { mutableStateOf(perfilSalvo.endereco) }
    var salvando by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "idoso_teste"
        SeniorLinkPrefs.carregarPerfilDoFirestore(uid)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fundoAtual)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Meu perfil",
            subtitulo = "Informações importantes para sua segurança.",
            onVoltar = onVoltar,
            escala = escala,
            cardFundo = cardFundo,
            tituloCor = tituloAtual,
            subtituloCor = subtituloAtual
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(if (altoContraste) Color.Yellow else AzulPrincipal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nome.trim().firstOrNull()?.uppercase() ?: "M",
                    fontSize = (32 * escala).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (altoContraste) Color.Black else Branco
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = nome.ifBlank { "Idoso" },
                fontSize = (18 * escala).sp,
                fontWeight = FontWeight.Bold,
                color = tituloAtual
            )
        }

        Spacer(Modifier.height(20.dp))

        CardSecao("Informações pessoais", escala = escala, cardFundo = cardFundo, tituloCor = tituloAtual) {

            Campo("Nome completo", nome, escala = escala) {
                nome = it
            }

            Campo("Idade", idade, escala = escala) {
                idade = it
            }

            Campo("Endereço residencial", endereco, escala = escala) {
                endereco = it
            }
        }

        CardSecao("Informações de saúde", escala = escala, cardFundo = cardFundo, tituloCor = tituloAtual) {

            Campo("Tipo sanguíneo", sangue, escala = escala) {
                sangue = it
            }

            Campo("Alergias", alergias, escala = escala) {
                alergias = it
            }

            Campo("Condições e medicamentos em uso", condicoes, escala = escala) {
                condicoes = it
            }
        }

        CardSecao("Contato de emergência", escala = escala, cardFundo = cardFundo, tituloCor = tituloAtual) {

            Campo("Telefone do contato", contatoEmergencia, escala = escala) {
                contatoEmergencia = it
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                salvando = true
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "idoso_teste"
                val novoPerfil = perfilSalvo.copy(
                    uid = uid,
                    nome = nome.trim(),
                    idade = idade.trim(),
                    tipoSanguineo = sangue.trim(),
                    alergias = alergias.trim(),
                    condicoesMedicas = condicoes.trim(),
                    contatoEmergencia = contatoEmergencia.trim(),
                    endereco = endereco.trim()
                )
                SeniorLinkPrefs.salvarPerfilNoFirestore(
                    perfil = novoPerfil,
                    onSuccess = {
                        salvando = false
                        Toast.makeText(context, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    onError = { erro ->
                        salvando = false
                        Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                    }
                )
            },
            enabled = !salvando,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text(
                text = if (salvando) "Salvando..." else "Salvar informações",
                fontSize = (17 * escala).sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))

        RodapeTela(subtituloCor = subtituloAtual)
    }
}


// ============================================================
// LOCALIZAÇÃO (TEMPO REAL DO IDOSO E MAPA DO RESPONSÁVEL)
// ============================================================

@Composable
private fun MapaLocalizacao(
    location: Location,
    tituloMarcador: String = "Idoso",
    modifier: Modifier = Modifier
) {

    val posicao = LatLng(
        location.latitude,
        location.longitude
    )

    val cameraPositionState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    posicao,
                    16f
                )
        }

    val markerState =
        rememberUpdatedMarkerState(
            position = posicao
        )

    LaunchedEffect(
        location.latitude,
        location.longitude
    ) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                posicao,
                16f
            )
        )
        markerState.position = posicao
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isBuildingEnabled = true,
            isTrafficEnabled = false,
            isIndoorEnabled = false
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            rotationGesturesEnabled = true
        )
    ) {
        Marker(
            state = markerState,
            title = tituloMarcador,
            snippet = "Localização atual do SeniorLink"
        )
    }
}

@Composable
fun LocalizacaoScreen(
    onVoltar: () -> Unit,
    idosoId: String = ""
) {
    val context = LocalContext.current
    val locationRepository = remember { LocationRepository(context) }

    var localizacaoLocal by remember { mutableStateOf<Location?>(null) }
    var latRemota by remember { mutableStateOf<Double?>(null) }
    var lngRemota by remember { mutableStateOf<Double?>(null) }
    var precisaoRemota by remember { mutableStateOf<Float?>(null) }
    var nomeIdosoRemoto by remember { mutableStateOf("Idoso") }

    var rastreamentoAtivo by remember { mutableStateOf(false) }
    var mensagemStatus by remember { mutableStateOf("Aguardando sinal de GPS...") }

    // MODO RESPONSÁVEL: Escuta a localização do idoso no Firebase Realtime Database
    DisposableEffect(idosoId) {
        if (idosoId.isNotBlank()) {
            val ref = FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nomeIdosoRemoto = snapshot.child("nome").getValue(String::class.java) ?: "Idoso"
                    val locSnap = snapshot.child("localizacao")
                    val lat = locSnap.child("latitude").getValue(Double::class.java)
                    val lng = locSnap.child("longitude").getValue(Double::class.java)
                    val prec = locSnap.child("precisao").getValue(Double::class.java)?.toFloat()

                    if (lat != null && lng != null) {
                        latRemota = lat
                        lngRemota = lng
                        precisaoRemota = prec
                        rastreamentoAtivo = true
                        mensagemStatus = "Localização de $nomeIdosoRemoto recebida em tempo real."
                    } else {
                        rastreamentoAtivo = false
                        mensagemStatus = "Aguardando $nomeIdosoRemoto abrir o aplicativo e liberar o GPS."
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mensagemStatus = "Erro ao buscar localização: ${error.message}"
                }
            }
            ref.addValueEventListener(listener)
            onDispose { ref.removeEventListener(listener) }
        } else {
            onDispose { }
        }
    }

    // MODO IDOSO: Captura o GPS do próprio aparelho
    fun possuiPermissaoLocalizacao(): Boolean {
        val permissaoPrecisa = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permissaoAproximada = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return permissaoPrecisa || permissaoAproximada
    }

    fun iniciarLocalizacaoLocal() {
        if (!possuiPermissaoLocalizacao()) {
            mensagemStatus = "Permissão de localização necessária."
            return
        }

        locationRepository.iniciarAtualizacao { location ->
            localizacaoLocal = location
            rastreamentoAtivo = true
            mensagemStatus = "Localização atualizada."
        }
    }

    val permissaoLocalizacaoLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->
            val precisa = permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val aproximada = permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (precisa || aproximada) {
                mensagemStatus = "Permissão concedida."
                iniciarLocalizacaoLocal()
            } else {
                mensagemStatus = "A permissão de localização foi negada."
            }
        }

    DisposableEffect(Unit) {
        if (idosoId.isBlank() && possuiPermissaoLocalizacao()) {
            iniciarLocalizacaoLocal()
        }
        onDispose {
            locationRepository.pararAtualizacao()
        }
    }

    val locationParaMapa: Location? = remember(latRemota, lngRemota, localizacaoLocal, idosoId) {
        if (idosoId.isNotBlank() && latRemota != null && lngRemota != null) {
            Location("firebase").apply {
                latitude = latRemota!!
                longitude = lngRemota!!
                accuracy = precisaoRemota ?: 10f
            }
        } else {
            localizacaoLocal
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = if (idosoId.isNotBlank()) "Localização de $nomeIdosoRemoto" else "Localização",
            subtitulo = "Acompanhe a localização em tempo real.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (rastreamentoAtivo) VerdeClaro else AzulClaro
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .background(
                            if (rastreamentoAtivo) Verde else AzulPrincipal,
                            RoundedCornerShape(50)
                        )
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (rastreamentoAtivo) "Localização ativa" else "Localização aguardando",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (rastreamentoAtivo) Verde else AzulPrincipal
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = mensagemStatus,
                        fontSize = 12.sp,
                        color = TextoSecundario
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Branco)
        ) {
            if (locationParaMapa != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp)
                ) {
                    MapaLocalizacao(
                        location = locationParaMapa,
                        tituloMarcador = if (idosoId.isNotBlank()) nomeIdosoRemoto else "Idoso",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.ic_localizacao),
                            contentDescription = "Localização",
                            modifier = Modifier.size(76.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Aguardando localização",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = mensagemStatus,
                            fontSize = 13.sp,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        CardSecao(titulo = "Informações da localização") {
            LinhaInformacao(
                "Latitude",
                locationParaMapa?.latitude?.toString() ?: "Ainda não disponível"
            )

            LinhaInformacao(
                "Longitude",
                locationParaMapa?.longitude?.toString() ?: "Ainda não disponível"
            )

            LinhaInformacao(
                "Precisão",
                locationParaMapa?.accuracy?.let { "${it.toInt()} metros" } ?: "Ainda não disponível"
            )

            LinhaInformacao(
                "Status",
                if (rastreamentoAtivo) "Atualizando" else "Aguardando"
            )
        }

        if (idosoId.isBlank()) {
            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    if (possuiPermissaoLocalizacao()) {
                        iniciarLocalizacaoLocal()
                    } else {
                        permissaoLocalizacaoLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
            ) {
                Text(
                    text = if (rastreamentoAtivo) "Atualizar localização" else "Ativar localização",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))

            if (localizacaoLocal != null) {
                OutlinedButton(
                    onClick = {
                        locationRepository.pararAtualizacao()
                        rastreamentoAtivo = false
                        mensagemStatus = "Atualização de localização pausada."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Pausar localização",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        RodapeTela()
    }
}


// ============================================================
// LEMBRETES (ISOLADOS POR IDOSO + ALARME NA TELA BLOQUEADA)
// ============================================================

@Composable
fun LembretesScreen(
    onVoltar: () -> Unit,
    idosoId: String = "",
    @Suppress("UNUSED_PARAMETER")
    onNovoLembrete: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repo = remember { FirestoreRepository() }

    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }
    val idAlvo = remember(idosoId) {
        idosoId.ifBlank { prefs.getString("id_idoso", "") ?: "" }
    }

    val lembretes by repo.observarLembretesPorIdoso(idAlvo).collectAsState(initial = emptyList())

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(lembretes) {
        if (idosoId.isBlank()) {
            lembretes.forEach { item ->
                if (!item.concluido) {
                    LembreteScheduler.agendarLembrete(context, item.id, item.titulo, item.horario, item.categoria)
                } else {
                    LembreteScheduler.cancelarLembrete(context, item.id)
                }
            }
        }
    }

    var mostrarNovoModal by remember { mutableStateOf(false) }

    val concluidos = lembretes.count { it.concluido }
    val total = lembretes.size
    val progresso = if (total > 0) concluidos.toFloat() / total else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 22.dp,
                    top = 42.dp,
                    end = 22.dp,
                    bottom = 20.dp
                )
        ) {
            CabecalhoTela(
                titulo = "Meus Lembretes",
                subtitulo = "Avisos sonoros mesmo com tela bloqueada.",
                onVoltar = onVoltar
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = AzulEscuro,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Rotina de Hoje",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Branco
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (total == 0) "Nenhum lembrete agendado" else "$concluidos de $total concluídos",
                                fontSize = 14.sp,
                                color = Branco.copy(alpha = 0.85f)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = Verde,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${(progresso * 100).toInt()}%",
                                    color = Branco,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { progresso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Verde,
                        trackColor = Branco.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (lembretes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏰", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tudo tranquilo por aqui!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )
                        Text(
                            text = "Toque no botão abaixo para criar um aviso.",
                            fontSize = 14.sp,
                            color = TextoSecundario
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ) {
                    items(lembretes, key = { it.id }) { lembrete ->
                        CardLembreteBonito(
                            lembrete = lembrete,
                            onAlternarConcluido = {
                                coroutineScope.launch {
                                    repo.alternarConcluidoLembretePorIdoso(idAlvo, lembrete.id, lembrete.concluido)
                                }
                            },
                            onExcluir = {
                                coroutineScope.launch {
                                    LembreteScheduler.cancelarLembrete(context, lembrete.id)
                                    repo.excluirLembretePorIdoso(idAlvo, lembrete.id)
                                    Toast.makeText(context, "Lembrete removido", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = { mostrarNovoModal = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp)
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Verde),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "+ Criar Novo Lembrete",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Branco
            )
        }

        if (mostrarNovoModal) {
            var tituloNovo by remember { mutableStateOf("") }
            var categoriaSelecionada by remember { mutableStateOf("Remédio") }
            var horarioSelecionado by remember { mutableStateOf("08:00") }

            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hora, minuto ->
                    horarioSelecionado = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            AlertDialog(
                onDismissRequest = { mostrarNovoModal = false },
                shape = RoundedCornerShape(24.dp),
                containerColor = Branco,
                title = {
                    Text(
                        text = "Novo Lembrete",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulEscuro
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OutlinedTextField(
                            value = tituloNovo,
                            onValueChange = { tituloNovo = it },
                            label = {
                                Text(
                                    text = "O que lembrar?",
                                    color = Color(0xFF123B63),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Ex: Tomar Losartana 50mg",
                                    color = Color(0xFF66788A)
                                )
                            },
                            textStyle = TextStyle(
                                color = Color(0xFF0A2E4D),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF0A2E4D),
                                unfocusedTextColor = Color(0xFF0A2E4D),
                                disabledTextColor = Color(0xFF0A2E4D),
                                focusedContainerColor = Color(0xFFF0F4F8),
                                unfocusedContainerColor = Color(0xFFF0F4F8),
                                cursorColor = Color(0xFF079B91),
                                focusedBorderColor = Color(0xFF079B91),
                                unfocusedBorderColor = Color(0xFF1764A5),
                                focusedLabelColor = Color(0xFF079B91),
                                unfocusedLabelColor = Color(0xFF123B63),
                                selectionColors = TextSelectionColors(
                                    handleColor = Color(0xFF079B91),
                                    backgroundColor = Color(0xFF079B91).copy(alpha = 0.25f)
                                )
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clickable { timePickerDialog.show() },
                            shape = RoundedCornerShape(16.dp),
                            color = Fundo,
                            border = BorderStroke(1.5.dp, Verde)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Horário do Alarme:", fontSize = 15.sp, color = TextoSecundario, fontWeight = FontWeight.Medium)
                                Text("⏰ $horarioSelecionado", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = AzulEscuro)
                            }
                        }

                        Text("Categoria:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AzulEscuro)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Remédio", "Consulta", "Água / Rotina").forEach { cat ->
                                val ativo = categoriaSelecionada == cat
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clickable { categoriaSelecionada = cat },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (ativo) AzulEscuro else Fundo,
                                    border = BorderStroke(1.dp, if (ativo) AzulEscuro else Borda)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = cat,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (ativo) Branco else AzulEscuro
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tituloNovo.isBlank()) {
                                Toast.makeText(context, "Digite o nome do lembrete.", Toast.LENGTH_SHORT).show()
                            } else if (idAlvo.isBlank()) {
                                Toast.makeText(context, "Nenhum idoso identificado.", Toast.LENGTH_SHORT).show()
                            } else {
                                coroutineScope.launch {
                                    repo.salvarLembretePorIdoso(idAlvo, tituloNovo.trim(), horarioSelecionado, categoriaSelecionada)
                                    Toast.makeText(context, "Alarme agendado para às $horarioSelecionado!", Toast.LENGTH_SHORT).show()
                                    mostrarNovoModal = false
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Verde)
                    ) {
                        Text("Agendar Alarme", color = Branco, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarNovoModal = false }) {
                        Text("Cancelar", color = TextoSecundario)
                    }
                }
            )
        }
    }
}

@Composable
private fun CardLembreteBonito(
    lembrete: LembreteFirestore,
    onAlternarConcluido: () -> Unit,
    onExcluir: () -> Unit
) {
    val corCategoria = when (lembrete.categoria) {
        "Remédio" -> Verde
        "Consulta" -> AzulPrincipal
        else -> Color(0xFFE67E22)
    }

    val iconeEmoji = when (lembrete.categoria) {
        "Remédio" -> "💊"
        "Consulta" -> "🩺"
        else -> "💧"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlternarConcluido() },
        shape = RoundedCornerShape(20.dp),
        color = if (lembrete.concluido) Color(0xFFEFF4F6) else Branco,
        shadowElevation = if (lembrete.concluido) 0.dp else 3.dp,
        border = BorderStroke(
            width = if (lembrete.concluido) 1.dp else 1.5.dp,
            color = if (lembrete.concluido) Borda else corCategoria.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (lembrete.concluido) Color.LightGray.copy(alpha = 0.3f) else corCategoria.copy(alpha = 0.12f),
                modifier = Modifier
                    .width(76.dp)
                    .height(68.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = iconeEmoji, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = lembrete.horario,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (lembrete.concluido) TextoSecundario else corCategoria
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lembrete.categoria.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (lembrete.concluido) TextoSecundario else corCategoria
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lembrete.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (lembrete.concluido) TextoSecundario else AzulEscuro,
                    textDecoration = if (lembrete.concluido) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (lembrete.concluido) "✓ Concluído" else "Toque para marcar como feito",
                    fontSize = 12.sp,
                    color = if (lembrete.concluido) Verde else TextoSecundario,
                    fontWeight = if (lembrete.concluido) FontWeight.Bold else FontWeight.Normal
                )
            }

            IconButton(onClick = onExcluir) {
                Text("🗑️", fontSize = 18.sp)
            }
        }
    }
}


// ============================================================
// APRENDER
// ============================================================

@Composable
fun AprenderScreen(
    onVoltar: () -> Unit
) {

    var aberto by remember {
        mutableIntStateOf(-1)
    }

    val tutoriais = listOf(
        "Aumentar o tamanho das letras" to
                "Abra Configurações, procure por Tela e ajuste o tamanho da fonte.",

        "Fazer uma ligação" to
                "Abra o aplicativo Telefone, escolha um contato e toque no botão de ligar.",

        "Usar o Wi-Fi" to
                "Abra Configurações, entre em Wi-Fi e selecione uma rede conhecida.",

        "Enviar uma mensagem" to
                "Abra o aplicativo de mensagens, escolha um contato, escreva e toque em enviar."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Aprender",
            subtitulo = "Aprenda a usar o celular passo a passo.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(24.dp))

        tutoriais.forEachIndexed { index, tutorial ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        aberto =
                            if (aberto == index) {
                                -1
                            } else {
                                index
                            }
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Branco
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = tutorial.first,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    if (aberto == index) {

                        Spacer(Modifier.height(12.dp))

                        HorizontalDivider(
                            color = Borda
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = tutorial.second,
                            fontSize = 15.sp,
                            lineHeight = 23.sp,
                            color = Texto
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                aberto = -1
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Fechar tutorial")
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(18.dp))

        RodapeTela()
    }
}


// ============================================================
// EMERGÊNCIA COM ALERTA E LOCALIZAÇÃO EM TEMPO REAL (BLINDADO)
// ============================================================

@Composable
fun EmergenciaScreen(
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }

    // Fallback robusto para garantir que o idosoId nunca fique em branco
    val authUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val idosoId = remember {
        val saved = prefs.getString("id_idoso", "") ?: ""
        if (saved.isNotBlank()) saved else authUid
    }
    val nomeIdoso = remember {
        val savedName = prefs.getString("nome_idoso", "") ?: ""
        if (savedName.isNotBlank()) savedName else (FirebaseAuth.getInstance().currentUser?.displayName ?: "Idoso")
    }

    val repo = remember { FirestoreRepository() }
    val locationRepo = remember { LocationRepository(context) }

    var confirmar by remember { mutableStateOf(false) }
    var alertaAtivo by remember { mutableStateOf(false) }
    var ultimaLoc by remember { mutableStateOf<Location?>(null) }

    DisposableEffect(alertaAtivo) {
        if (alertaAtivo && idosoId.isNotBlank()) {
            val temPermissao = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (temPermissao) {
                locationRepo.iniciarAtualizacao { loc ->
                    ultimaLoc = loc
                    val dadosLoc = mapOf(
                        "latitude" to loc.latitude,
                        "longitude" to loc.longitude,
                        "precisao" to loc.accuracy.toDouble(),
                        "atualizadoEm" to System.currentTimeMillis()
                    )
                    // Atualiza Realtime Database diretamente para entrega imediata ao responsável
                    FirebaseDatabase.getInstance()
                        .getReference("usuarios")
                        .child(idosoId)
                        .child("localizacao")
                        .setValue(dadosLoc)

                    FirebaseDatabase.getInstance()
                        .getReference("emergencias")
                        .child(idosoId)
                        .setValue(
                            mapOf(
                                "idosoId" to idosoId,
                                "nome" to nomeIdoso,
                                "latitude" to loc.latitude,
                                "longitude" to loc.longitude,
                                "timestamp" to System.currentTimeMillis(),
                                "status" to "ativo"
                            )
                        )

                    repo.dispararEmergencia(idosoId, nomeIdoso, loc.latitude, loc.longitude) {}
                }
            } else {
                repo.dispararEmergencia(idosoId, nomeIdoso, null, null) {}
            }
        }
        onDispose {
            locationRepo.pararAtualizacao()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 22.dp,
                top = 42.dp,
                end = 22.dp,
                bottom = 24.dp
            )
    ) {

        CabecalhoTela(
            titulo = "Emergência",
            subtitulo = "Peça ajuda quando precisar.",
            onVoltar = onVoltar
        )

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (alertaAtivo) Vermelho else VermelhoClaro
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .background(
                            Branco,
                            RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_emergencia
                        ),
                        contentDescription = "Emergência",
                        modifier = Modifier.size(68.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (alertaAtivo) "ALERTA ENVIADO!" else "Precisa de ajuda?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (alertaAtivo) Branco else Vermelho,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (alertaAtivo) {
                        "Seu responsável foi notificado e está acompanhando sua localização em tempo real."
                    } else {
                        "Toque no botão abaixo para pedir ajuda com sua localização em tempo real."
                    },
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = if (alertaAtivo) Branco else Texto,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                if (!alertaAtivo) {
                    Button(
                        onClick = { confirmar = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        Text(
                            text = "PEDIR AJUDA",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Branco
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            alertaAtivo = false
                            FirebaseDatabase.getInstance().getReference("emergencias").child(idosoId).removeValue()
                            FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId).child("alertaAtivo").setValue(false)
                            repo.encerrarEmergencia(idosoId)
                            Toast.makeText(context, "Alerta encerrado.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Branco)
                    ) {
                        Text(
                            text = "CANCELAR ALERTA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Vermelho
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        RodapeTela()
    }

    if (confirmar) {

        AlertDialog(
            onDismissRequest = { confirmar = false },
            containerColor = Branco,

            title = {
                Text(
                    text = "Confirmar pedido de ajuda?",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Vermelho
                )
            },

            text = {
                Text(
                    text = "Um alerta urgente será enviado imediatamente para o painel do seu responsável junto com sua localização em tempo real.",
                    color = Texto,
                    fontSize = 16.sp
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        confirmar = false
                        alertaAtivo = true

                        // Gravação forçada em Realtime Database para garantir envio ao responsável
                        val emergencyData = mapOf(
                            "idosoId" to idosoId,
                            "nome" to nomeIdoso,
                            "latitude" to (ultimaLoc?.latitude ?: 0.0),
                            "longitude" to (ultimaLoc?.longitude ?: 0.0),
                            "timestamp" to System.currentTimeMillis(),
                            "status" to "ativo"
                        )
                        FirebaseDatabase.getInstance().getReference("emergencias").child(idosoId).setValue(emergencyData)
                        FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId).child("alertaAtivo").setValue(true)

                        repo.dispararEmergencia(
                            idosoId = idosoId,
                            nomeIdoso = nomeIdoso,
                            latitude = ultimaLoc?.latitude,
                            longitude = ultimaLoc?.longitude
                        ) {
                            Toast.makeText(context, "Alerta urgente enviado!", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                ) {
                    Text(
                        "SIM, PEDIR AJUDA",
                        color = Branco,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(onClick = { confirmar = false }) {
                    Text(
                        "Cancelar",
                        color = AzulPrincipal
                    )
                }
            }
        )
    }
}


// ============================================================
// CABEÇALHO
// ============================================================

@Composable
private fun CabecalhoTela(
    titulo: String,
    subtitulo: String,
    onVoltar: () -> Unit,
    escala: Float = 1.0f,
    cardFundo: Color = Branco,
    tituloCor: Color = AzulEscuro,
    subtituloCor: Color = TextoSecundario
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    cardFundo,
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    onVoltar()
                },
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(
                    R.drawable.ic_voltar
                ),
                contentDescription = "Voltar",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                fontSize = (28 * escala).sp,
                fontWeight = FontWeight.Bold,
                color = tituloCor
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitulo,
                fontSize = (14 * escala).sp,
                lineHeight = (19 * escala).sp,
                color = subtituloCor
            )
        }
    }
}


// ============================================================
// CARD DE SEÇÃO
// ============================================================

@Composable
private fun CardSecao(
    titulo: String,
    escala: Float = 1.0f,
    cardFundo: Color = Branco,
    tituloCor: Color = AzulEscuro,
    conteudo: @Composable () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardFundo
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = titulo,
                fontSize = (19 * escala).sp,
                fontWeight = FontWeight.Bold,
                color = tituloCor
            )

            conteudo()
        }
    }
}


// ============================================================
// CAMPO
// ============================================================

@Composable
private fun Campo(
    titulo: String,
    valor: String,
    escala: Float = 1.0f,
    aoAlterar: (String) -> Unit
) {

    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = (16 * escala).sp),
        label = {
            Text(titulo, fontSize = (14 * escala).sp)
        }
    )
}


// ============================================================
// INFORMAÇÃO
// ============================================================

@Composable
private fun LinhaInformacao(
    titulo: String,
    valor: String
) {

    Column {

        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextoSecundario
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = valor,
            fontSize = 16.sp,
            color = Texto
        )

        Spacer(Modifier.height(10.dp))
    }
}


// ============================================================
// FOOTER
// ============================================================

@Composable
private fun RodapeTela(
    subtituloCor: Color = TextoSecundario
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalDivider(
            color = Borda,
            thickness = 1.dp
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "SeniorLink",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrincipal
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = "Cuidado, autonomia e conexão",
            fontSize = 11.sp,
            color = subtituloCor
        )

        Spacer(Modifier.height(10.dp))
    }
}