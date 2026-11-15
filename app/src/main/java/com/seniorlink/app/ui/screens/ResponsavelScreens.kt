package com.seniorlink.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.R
import com.seniorlink.app.data.FirestoreRepository
import com.seniorlink.app.data.PerfilVinculadoFirestore
import com.seniorlink.app.services.AlertaBackgroundService

private val AzulEscuro = Color(0xFF123B63)
private val AzulPrincipal = Color(0xFF06466B)
private val VerdeMarca = Color(0xFF159C8C)
private val FundoCinza = Color(0xFFF6FAFB)
private val TextoSuave = Color(0xFF52677A)
private val BordaCard = Color(0xFFD7E3E8)
private val VerdeSuave = Color(0xFFE6F4F1)
private val AlertaFundo = Color(0xFFFFF4E5)
private val AlertaTexto = Color(0xFFB25E09)

@Composable
fun PainelResponsavelScreen(
    onVoltar: () -> Unit,
    onIrParaLocalizacao: (String) -> Unit,
    onIrParaAgenda: (String) -> Unit,
    onIrParaFicha: () -> Unit,
    onIrParaContatos: (String) -> Unit,
    onIrParaTreinamento: () -> Unit,
    onAdicionarIdoso: () -> Unit
) {
    val context = LocalContext.current
    val authEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val firestoreRepo = remember { FirestoreRepository() }

    // INICIA O SERVIÇO EM SEGUNDO PLANO PARA FUNCIONAR COM O APP FECHADO
    LaunchedEffect(Unit) {
        val serviceIntent = Intent(context, AlertaBackgroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    val listaVinculados by firestoreRepo.observarIdososDoResponsavel(authEmail).collectAsState(initial = emptyList())

    var idosoSelecionadoId by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(listaVinculados) {
        if (listaVinculados.isNotEmpty() && listaVinculados.none { it.id == idosoSelecionadoId }) {
            idosoSelecionadoId = listaVinculados.first().id
        }
    }

    val idosoAtual: PerfilVinculadoFirestore? = listaVinculados.find { it.id == idosoSelecionadoId }

    // MONITORAMENTO GLOBAL: Escuta em tempo real se há algum idoso em emergência
    var idosoEmEmergenciaId by remember { mutableStateOf<String?>(null) }

    DisposableEffect(listaVinculados) {
        if (listaVinculados.isEmpty()) {
            return@DisposableEffect onDispose {}
        }

        val activeListeners = mutableListOf<Pair<com.google.firebase.database.DatabaseReference, ValueEventListener>>()

        listaVinculados.forEach { idoso ->
            val id = idoso.id
            val refEmergencias = FirebaseDatabase.getInstance().getReference("emergencias").child(id)
            val refUsuarioAtivo = FirebaseDatabase.getInstance().getReference("usuarios").child(id).child("emergencia_ativa")

            val checkAtivo = { snapshot: DataSnapshot ->
                snapshot.child("ativo").getValue(Boolean::class.java) == true
            }

            val listenerRaiz = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (checkAtivo(snapshot)) {
                        idosoEmEmergenciaId = id
                    } else if (idosoEmEmergenciaId == id) {
                        idosoEmEmergenciaId = null
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }

            val listenerUsuario = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (checkAtivo(snapshot)) {
                        idosoEmEmergenciaId = id
                    } else if (idosoEmEmergenciaId == id) {
                        idosoEmEmergenciaId = null
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }

            refEmergencias.addValueEventListener(listenerRaiz)
            refUsuarioAtivo.addValueEventListener(listenerUsuario)

            activeListeners.add(refEmergencias to listenerRaiz)
            activeListeners.add(refUsuarioAtivo to listenerUsuario)
        }

        onDispose {
            activeListeners.forEach { (ref, listener) ->
                ref.removeEventListener(listener)
            }
        }
    }

    val emergenciaAtiva = idosoEmEmergenciaId != null

    var telaAtual by rememberSaveable { mutableStateOf("painel") }
    var idosoEmEdicao by remember { mutableStateOf<PerfilVinculadoFirestore?>(null) }

    LaunchedEffect(listaVinculados) {
        val pendente = listaVinculados.firstOrNull { !it.cadastroCompleto }
        if (pendente != null && telaAtual == "painel" && idosoEmEdicao == null) {
            idosoEmEdicao = pendente
            telaAtual = "ficha_detalhada"
        }
    }

    when (telaAtual) {
        "novo_idoso" -> {
            TelaCadastroBasicoIdoso(
                authEmail = authEmail,
                firestoreRepo = firestoreRepo,
                onVoltar = { telaAtual = "painel" },
                onIdosoCriado = { recemCriado ->
                    idosoSelecionadoId = recemCriado.id
                    idosoEmEdicao = recemCriado
                    telaAtual = "ficha_detalhada"
                }
            )
        }

        "ficha_detalhada" -> {
            val alvo = idosoEmEdicao ?: idosoAtual
            if (alvo != null) {
                TelaFichaAprofundadaIdoso(
                    idoso = alvo,
                    firestoreRepo = firestoreRepo,
                    onVoltar = {
                        idosoEmEdicao = null
                        telaAtual = "painel"
                    },
                    onConcluido = {
                        idosoEmEdicao = null
                        telaAtual = "painel"
                    }
                )
            } else {
                telaAtual = "painel"
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FundoCinza)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_voltar),
                            contentDescription = "Voltar",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { onVoltar() },
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Painel do Responsável",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulPrincipal
                            )
                            Text(
                                text = "Acompanhamento e Gestão de Cuidados",
                                fontSize = 13.sp,
                                color = TextoSuave
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // BANNER VERMELHO URGENTE: Abre o Google Maps externo diretamente com a localização do idoso
                    val idosoEmEmergencia = listaVinculados.find { it.id == idosoEmEmergenciaId }
                    if (emergenciaAtiva && idosoEmEmergencia != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 18.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFD63636),
                            shadowElevation = 6.dp
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "🚨 PEDIDO DE AJUDA URGENTE!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${idosoEmEmergencia.nome} acionou o botão de emergência!",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = {
                                            val id = idosoEmEmergencia.id
                                            FirebaseDatabase.getInstance().getReference("localizacoes").child(id)
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val lat = snapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                                                        val lng = snapshot.child("longitude").getValue(Double::class.java) ?: 0.0

                                                        if (lat != 0.0 && lng != 0.0) {
                                                            val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(idosoEmEmergencia.nome)})")
                                                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                                            try {
                                                                context.startActivity(mapIntent)
                                                            } catch (e: Exception) {
                                                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng"))
                                                                context.startActivity(webIntent)
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Aguardando sinal de GPS do idoso...", Toast.LENGTH_SHORT).show()
                                                            onIrParaLocalizacao(id)
                                                        }
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {
                                                        onIrParaLocalizacao(id)
                                                    }
                                                })
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "ABRIR NO MAPS",
                                            color = Color(0xFFD63636),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val id = idosoEmEmergencia.id
                                            FirebaseDatabase.getInstance().getReference("emergencias").child(id).child("ativo").setValue(false)
                                            FirebaseDatabase.getInstance().getReference("usuarios").child(id).child("emergencia_ativa").child("ativo").setValue(false)
                                            idosoEmEmergenciaId = null
                                            Toast.makeText(context, "Alarme desativado com sucesso.", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AzulEscuro),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "PARAR ALARME",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Idosos vinculados",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulPrincipal
                        )

                        Button(
                            onClick = { telaAtual = "novo_idoso" },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeMarca),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            Text(
                                text = "+ Adicionar Idoso",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (listaVinculados.isEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { telaAtual = "novo_idoso" },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BordaCard)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Nenhum idoso vinculado. Toque para adicionar.",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextoSuave
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listaVinculados.forEach { idoso ->
                                AbasIdosoItem(
                                    nome = idoso.nome,
                                    selecionado = idoso.id == idosoAtual?.id,
                                    onClick = { idosoSelecionadoId = idoso.id }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (idosoAtual != null && !idosoAtual.cadastroCompleto) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .clickable {
                                    idosoEmEdicao = idosoAtual
                                    telaAtual = "ficha_detalhada"
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = AlertaFundo,
                            border = BorderStroke(1.dp, AlertaTexto.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ficha clínica pendente",
                                        fontWeight = FontWeight.Bold,
                                        color = AlertaTexto,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Complete os dados médicos e de emergência de ${idosoAtual.nome}.",
                                        fontSize = 12.sp,
                                        color = AlertaTexto
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = AlertaTexto
                                ) {
                                    Text(
                                        text = "Preencher",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        color = Color.White,
                        shadowElevation = 3.dp,
                        border = BorderStroke(1.dp, BordaCard)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarIniciais(
                                    nome = idosoAtual?.nome ?: "Idoso",
                                    modifier = Modifier.size(54.dp)
                                )

                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = idosoAtual?.nome ?: "Nenhum idoso",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulPrincipal
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = if (idosoAtual != null) "Acesso do idoso configurado | Idade: ${idosoAtual.idade.ifBlank { "Não informada" }}" else "Cadastre um idoso para começar",
                                        fontSize = 12.sp,
                                        color = VerdeMarca,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = BordaCard, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(16.dp))

                            InfoLinhaResumida(
                                rotulo = "Condições Médicas e Tipo Sanguíneo:",
                                valor = if (idosoAtual == null) "-" else "${idosoAtual.condicoesMedicas.ifBlank { "Nenhuma informada" }} | Sangue: ${idosoAtual.tipoSanguineo.ifBlank { "Não inf." }}"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            InfoLinhaResumida(
                                rotulo = "Medicamentos e Alergias:",
                                valor = if (idosoAtual == null) "-" else "Remédios: ${idosoAtual.medicamentos.ifBlank { "Nenhum" }} | Alergias: ${idosoAtual.alergias.ifBlank { "Nenhuma" }}"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            InfoLinhaResumida(
                                rotulo = "Endereço e Contato:",
                                valor = if (idosoAtual == null) "-" else "${idosoAtual.endereco.ifBlank { "Endereço pendente" }} | Tel: ${idosoAtual.telefone.ifBlank { "-" }}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Gerenciamento e Rotina",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CardAcaoRapida(
                            titulo = "Localização Exata",
                            subtitulo = "Ver mapa de ${idosoAtual?.nome ?: "idoso"}",
                            iconeRes = R.drawable.ic_localizacao,
                            modifier = Modifier.weight(1f),
                            onClick = { onIrParaLocalizacao(idosoAtual?.id ?: "") }
                        )
                        CardAcaoRapida(
                            titulo = "Agenda e Lembretes",
                            subtitulo = "Gerenciar rotina e avisos",
                            iconeRes = R.drawable.ic_lembretes,
                            modifier = Modifier.weight(1f),
                            onClick = { onIrParaAgenda(idosoAtual?.id ?: "") }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CardAcaoRapida(
                            titulo = "Ficha Clínica",
                            subtitulo = "Editar dados médicos",
                            iconeRes = R.drawable.icon_idoso,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (idosoAtual != null) {
                                    idosoEmEdicao = idosoAtual
                                    telaAtual = "ficha_detalhada"
                                } else {
                                    onIrParaFicha()
                                }
                            }
                        )
                        CardAcaoRapida(
                            titulo = "Contatos e Rede",
                            subtitulo = "Equipe de apoio e médicos",
                            iconeRes = R.drawable.ic_contatos,
                            modifier = Modifier.weight(1f),
                            onClick = { onIrParaContatos(idosoAtual?.id ?: "") }
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedButton(
                        onClick = onIrParaTreinamento,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, VerdeMarca),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Acessar Treinamento do Responsável",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdeMarca
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AvatarIniciais(nome: String, modifier: Modifier = Modifier) {
    val iniciais = remember(nome) {
        val partes = nome.trim().split(" ").filter { it.isNotBlank() }
        when {
            partes.isEmpty() -> "SL"
            partes.size == 1 -> partes[0].take(2).uppercase()
            else -> "${partes[0].take(1)}${partes.last().take(1)}".uppercase()
        }
    }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = VerdeSuave,
        border = BorderStroke(1.dp, VerdeMarca.copy(alpha = 0.5f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = iniciais,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeMarca
            )
        }
    }
}

@Composable
private fun TelaCadastroBasicoIdoso(
    authEmail: String,
    firestoreRepo: FirestoreRepository,
    onVoltar: () -> Unit,
    onIdosoCriado: (PerfilVinculadoFirestore) -> Unit
) {
    val context = LocalContext.current
    var nomeNovo by remember { mutableStateOf("") }
    var pinNovo by remember { mutableStateOf("") }
    var verificando by remember { mutableStateOf(false) }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AzulEscuro,
        unfocusedTextColor = AzulEscuro,
        cursorColor = VerdeMarca,
        focusedBorderColor = VerdeMarca,
        unfocusedBorderColor = BordaCard,
        focusedLabelColor = VerdeMarca,
        unfocusedLabelColor = TextoSuave,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoCinza)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_voltar),
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(52.dp)
                    .clickable { if (!verificando) onVoltar() },
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Vincular Novo Idoso",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AzulPrincipal
            )
            Text(
                text = "Etapa 1 de 2 • Defina o nome e um PIN exclusivo de 4 números para o idoso entrar no aplicativo.",
                fontSize = 15.sp,
                color = TextoSuave,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, BordaCard)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = nomeNovo,
                        onValueChange = { nomeNovo = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome completo do idoso") },
                        placeholder = { Text("Ex: Maria Aparecida Silva") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = campoColors
                    )

                    OutlinedTextField(
                        value = pinNovo,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinNovo = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("PIN exclusivo de acesso (4 números)") },
                        placeholder = { Text("Ex: 4829") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = campoColors
                    )

                    Text(
                        text = "Este idoso ficará vinculado à conta: $authEmail",
                        fontSize = 12.sp,
                        color = TextoSuave
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                enabled = !verificando,
                onClick = {
                    if (nomeNovo.isBlank()) {
                        Toast.makeText(context, "Digite o nome completo do idoso.", Toast.LENGTH_SHORT).show()
                    } else if (pinNovo.length < 4) {
                        Toast.makeText(context, "O PIN precisa ter 4 números.", Toast.LENGTH_SHORT).show()
                    } else if (authEmail.isBlank()) {
                        Toast.makeText(context, "E-mail do responsável não autenticado.", Toast.LENGTH_SHORT).show()
                    } else {
                        verificando = true
                        firestoreRepo.verificarPinExiste(pinNovo) { jaExiste ->
                            if (jaExiste) {
                                verificando = false
                                Toast.makeText(context, "Este PIN já está em uso! Escolha outros 4 números.", Toast.LENGTH_LONG).show()
                            } else {
                                val ref = FirebaseDatabase.getInstance().getReference("usuarios").push()
                                val novoId = ref.key ?: ""
                                val dadosIdoso = mapOf(
                                    "nome" to nomeNovo,
                                    "pin" to pinNovo,
                                    "emailResponsavel" to authEmail,
                                    "tipo" to "idoso",
                                    "cadastroCompleto" to false
                                )

                                ref.setValue(dadosIdoso)
                                    .addOnSuccessListener {
                                        verificando = false
                                        val recemCriado = PerfilVinculadoFirestore(
                                            id = novoId,
                                            nome = nomeNovo,
                                            email = authEmail,
                                            tipo = "idoso",
                                            cadastroCompleto = false
                                        )
                                        onIdosoCriado(recemCriado)
                                    }
                                    .addOnFailureListener { e ->
                                        verificando = false
                                        Toast.makeText(context, "Erro ao salvar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeMarca)
            ) {
                if (verificando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(26.dp))
                } else {
                    Text(
                        text = "Continuar para Ficha Clínica ›",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun TelaFichaAprofundadaIdoso(
    idoso: PerfilVinculadoFirestore,
    firestoreRepo: FirestoreRepository,
    onVoltar: () -> Unit,
    onConcluido: () -> Unit
) {
    val context = LocalContext.current
    var idade by remember { mutableStateOf(idoso.idade) }
    var tipoSanguineo by remember { mutableStateOf(idoso.tipoSanguineo) }
    var telefone by remember { mutableStateOf(idoso.telefone) }
    var endereco by remember { mutableStateOf(idoso.endereco) }
    var condicoesMedicas by remember { mutableStateOf(idoso.condicoesMedicas) }
    var medicamentos by remember { mutableStateOf(idoso.medicamentos) }
    var alergias by remember { mutableStateOf(idoso.alergias) }
    var salvando by remember { mutableStateOf(false) }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AzulEscuro,
        unfocusedTextColor = AzulEscuro,
        cursorColor = VerdeMarca,
        focusedBorderColor = VerdeMarca,
        unfocusedBorderColor = BordaCard,
        focusedLabelColor = VerdeMarca,
        unfocusedLabelColor = TextoSuave,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoCinza)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_voltar),
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .size(52.dp)
                        .clickable { if (!salvando) onVoltar() },
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Ficha de ${idoso.nome}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulPrincipal
                    )
                    Text(
                        text = "Informações clínicas e de segurança",
                        fontSize = 13.sp,
                        color = TextoSuave
                    )
                }
            }

            Text("Dados Pessoais e Contato", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, BordaCard)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = idade,
                            onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) idade = it },
                            label = { Text("Idade *") },
                            placeholder = { Text("Ex: 74") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )

                        OutlinedTextField(
                            value = tipoSanguineo,
                            onValueChange = { tipoSanguineo = it.uppercase() },
                            label = { Text("Tipo Sang. *") },
                            placeholder = { Text("Ex: O+") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )
                    }

                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = { Text("Telefone / Contato de Emergência *") },
                        placeholder = { Text("(11) 99999-9999") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = campoColors
                    )

                    OutlinedTextField(
                        value = endereco,
                        onValueChange = { endereco = it },
                        label = { Text("Endereço Residencial Completo *") },
                        placeholder = { Text("Rua, número, bairro e cidade") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = campoColors
                    )
                }
            }

            Text("Histórico Médico e Rotina", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, BordaCard)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = condicoesMedicas,
                        onValueChange = { condicoesMedicas = it },
                        label = { Text("Condições Médicas / Doenças Crônicas") },
                        placeholder = { Text("Ex: Hipertensão, Diabetes, Alzheimer...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = campoColors
                    )

                    OutlinedTextField(
                        value = medicamentos,
                        onValueChange = { medicamentos = it },
                        label = { Text("Medicamentos de Uso Contínuo") },
                        placeholder = { Text("Ex: Losartana 50mg (manhã), AAS (almoço)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = campoColors
                    )

                    OutlinedTextField(
                        value = alergias,
                        onValueChange = { alergias = it },
                        label = { Text("Alergias (Remédios ou Alimentos)") },
                        placeholder = { Text("Ex: Dipirona, Penicilina ou Nenhuma") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = campoColors
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                enabled = !salvando,
                onClick = {
                    if (idade.isBlank() || tipoSanguineo.isBlank() || telefone.isBlank() || endereco.isBlank()) {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios (*).", Toast.LENGTH_SHORT).show()
                    } else {
                        salvando = true
                        firestoreRepo.atualizarFichaCompletaIdoso(
                            idosoId = idoso.id,
                            idade = idade,
                            tipoSanguineo = tipoSanguineo,
                            condicoesMedicas = condicoesMedicas,
                            alergias = alergias,
                            medicamentos = medicamentos,
                            endereco = endereco,
                            telefone = telefone,
                            onSuccess = {
                                salvando = false
                                Toast.makeText(context, "Ficha de ${idoso.nome} salva com sucesso!", Toast.LENGTH_SHORT).show()
                                onConcluido()
                            },
                            onError = { msg ->
                                salvando = false
                                Toast.makeText(context, "Erro ao salvar: $msg", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeMarca)
            ) {
                if (salvando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(26.dp))
                } else {
                    Text(
                        text = "Salvar Ficha Completa",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AbasIdosoItem(
    nome: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(44.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = if (selecionado) AzulPrincipal else Color.White,
        border = BorderStroke(1.dp, if (selecionado) AzulPrincipal else BordaCard),
        shadowElevation = if (selecionado) 3.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (selecionado) Color.White else AzulPrincipal,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun InfoLinhaResumida(rotulo: String, valor: String) {
    Column {
        Text(text = rotulo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextoSuave)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AzulPrincipal)
    }
}

@Composable
private fun CardAcaoRapida(
    titulo: String,
    subtitulo: String,
    iconeRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(108.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, BordaCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
                Image(
                    painter = painterResource(id = iconeRes),
                    contentDescription = titulo,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(text = subtitulo, fontSize = 12.sp, color = TextoSuave, lineHeight = 16.sp, maxLines = 2)
        }
    }
}