package com.seniorlink.app.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.database.FirebaseDatabase
import com.seniorlink.app.R
import com.seniorlink.app.auth.AuthViewModel
import com.seniorlink.app.data.FirestoreRepository
import kotlinx.coroutines.launch

// ============================================================
// CORES DO SENIORLINK
// ============================================================

private val Azul = Color(0xFF123B63)
private val AzulPrincipal = Color(0xFF06466B)
private val Verde = Color(0xFF159C8C)
private val Fundo = Color(0xFFF6FAFB)
private val Texto = Color(0xFF52677A)
private val Borda = Color(0xFFD7E3E8)

private const val WEB_CLIENT_ID = "214010694497-man1b5ob5lcnaj6oh1d121mkgip9p3rk.apps.googleusercontent.com"

// ============================================================
// TELA DE LOGIN
// ============================================================

@Composable
fun LoginSeniorLinkScreen(
    onEntrar: () -> Unit,
    onEntrarResponsavel: () -> Unit,
    onEntrarCuidador: () -> Unit,
    onCriarConta: () -> Unit,
    onEsqueciSenha: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val firestoreRepo = remember { FirestoreRepository() }

    val isLoading by authViewModel.isLoading.collectAsState()
    val authMessage by authViewModel.authMessage.collectAsState()

    var tipoAcesso by rememberSaveable { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    var validandoPin by remember { mutableStateOf(false) }

    LaunchedEffect(authMessage) {
        authMessage?.let { mensagem ->
            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
            authViewModel.limparMensagem()
        }
    }

    val acaoSucesso: () -> Unit = when (tipoAcesso) {
        "idoso" -> onEntrar
        "responsavel" -> onEntrarResponsavel
        "cuidador" -> onEntrarCuidador
        else -> onEntrarResponsavel
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.seniorlink_logo),
                contentDescription = "Logo SeniorLink",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (tipoAcesso == null) {
                Text(
                    text = "Bem-vindo ao SeniorLink",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrincipal,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Como você vai usar o aplicativo?",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    color = Texto,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))

                CardPerfilLoginUnificado(
                    titulo = "Sou idoso",
                    subtitulo = "Quero usar o SeniorLink",
                    iconeRes = R.drawable.icon_idoso,
                    imageModifier = Modifier.size(50.dp),
                    onClick = { tipoAcesso = "idoso" }
                )
                Spacer(modifier = Modifier.height(18.dp))

                CardPerfilLoginUnificado(
                    titulo = "Sou responsável",
                    subtitulo = "Quero acompanhar alguém",
                    iconeRes = R.drawable.icon_responsavel,
                    imageModifier = Modifier.size(38.dp),
                    onClick = { tipoAcesso = "responsavel" }
                )
                Spacer(modifier = Modifier.height(18.dp))

                CardPerfilLoginUnificado(
                    titulo = "Sou cuidador",
                    subtitulo = "Cuido de uma pessoa idosa",
                    iconeRes = R.drawable.icon_cuidador,
                    imageModifier = Modifier.size(68.dp),
                    onClick = { tipoAcesso = "cuidador" }
                )

                Spacer(modifier = Modifier.height(28.dp))

                TextButton(onClick = onCriarConta) {
                    Text("Criar uma conta", color = Verde, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_voltar),
                        contentDescription = "Voltar",
                        modifier = Modifier.size(52.dp).clickable { tipoAcesso = null },
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (tipoAcesso == "idoso") {
                    Text("Acesso do idoso", modifier = Modifier.fillMaxWidth(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Digite seu PIN para entrar", modifier = Modifier.fillMaxWidth(), fontSize = 17.sp, color = Texto, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) pin = it },
                        modifier = Modifier.fillMaxWidth().height(70.dp),
                        singleLine = true,
                        label = { Text("PIN de acesso", fontSize = 16.sp) },
                        placeholder = { Text("Digite 4 números", fontSize = 17.sp, color = Texto) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul, unfocusedTextColor = Azul,
                            focusedBorderColor = Verde, unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde, unfocusedLabelColor = Texto,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        enabled = !validandoPin,
                        onClick = {
                            if (pin.length < 4) {
                                Toast.makeText(context, "Digite os 4 números do PIN.", Toast.LENGTH_SHORT).show()
                            } else {
                                validandoPin = true
                                firestoreRepo.buscarIdosoPorPin(pin) { idosoEncontrado ->
                                    validandoPin = false
                                    if (idosoEncontrado != null) {
                                        val prefs = context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE)
                                        prefs.edit {
                                            putString("id_idoso", idosoEncontrado.id)
                                            putString("nome_idoso", idosoEncontrado.nome)
                                            putString("email_responsavel", idosoEncontrado.email)
                                        }
                                        Toast.makeText(context, "Bem-vindo(a), ${idosoEncontrado.nome}!", Toast.LENGTH_SHORT).show()
                                        onEntrar()
                                    } else {
                                        Toast.makeText(context, "PIN não encontrado. Verifique com seu responsável.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
                    ) {
                        if (validandoPin) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp))
                        } else {
                            Text("Entrar", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    TextButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Solicitação de ajuda enviada ao responsável.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ) {
                        Text("Preciso de ajuda para entrar", color = AzulPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    val titulo = if (tipoAcesso == "responsavel") "Acesso do responsável" else "Acesso do cuidador"

                    Text(titulo, modifier = Modifier.fillMaxWidth(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Entre com seus dados", modifier = Modifier.fillMaxWidth(), fontSize = 17.sp, color = Texto, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(26.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        singleLine = true,
                        label = { Text("E-mail", fontSize = 15.sp) },
                        placeholder = { Text("seuemail@exemplo.com", fontSize = 16.sp, color = Texto) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul, unfocusedTextColor = Azul,
                            focusedBorderColor = Verde, unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde, unfocusedLabelColor = Texto,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        singleLine = true,
                        label = { Text("Senha", fontSize = 15.sp) },
                        placeholder = { Text("Digite sua senha", fontSize = 16.sp, color = Texto) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        visualTransformation = if (mostrarSenha) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = { mostrarSenha = !mostrarSenha }) {
                                Text(if (mostrarSenha) "OCULTAR" else "MOSTRAR", color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul, unfocusedTextColor = Azul,
                            focusedBorderColor = Verde, unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde, unfocusedLabelColor = Texto,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = {
                            if (tipoAcesso == "responsavel") {
                                if (email.isBlank()) {
                                    Toast.makeText(context, "Digite seu e-mail acima para recuperar a senha.", Toast.LENGTH_LONG).show()
                                } else {
                                    authViewModel.recuperarSenha(email) { msg: String ->
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                onEsqueciSenha()
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        val textoAjuda = if (tipoAcesso == "responsavel") "Esqueci minha senha (E-mail)" else "Preciso de ajuda para entrar"
                        Text(textoAjuda, color = AzulPrincipal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            authViewModel.login(email, senha, acaoSucesso)
                        },
                        modifier = Modifier.fillMaxWidth().height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp))
                        } else {
                            Text("Entrar", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    GoogleSignInButton(
                        texto = "Entrar com o Google",
                        isLoading = isLoading,
                        onClick = {
                            coroutineScope.launch {
                                iniciarFluxoGoogle(context, authViewModel, acaoSucesso)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CardPerfilLoginUnificado(
    titulo: String,
    subtitulo: String,
    iconeRes: Int,
    imageModifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Color(0xFFEAF4FC), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconeRes),
                    contentDescription = titulo,
                    modifier = imageModifier,
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = subtitulo, fontSize = 14.sp, color = Texto)
            }

            Text(text = "›", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Verde)
        }
    }
}

// ============================================================
// TELA DE CADASTRO
// ============================================================

@Composable
fun CadastroSeniorLinkScreen(
    onVoltar: () -> Unit,
    onEntrarIdoso: () -> Unit,
    onEntrarResponsavel: () -> Unit,
    onEntrarCuidador: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val firestoreRepo = remember { FirestoreRepository() }

    val isLoading by authViewModel.isLoading.collectAsState()
    val authMessage by authViewModel.authMessage.collectAsState()

    var perfilSelecionado by rememberSaveable { mutableStateOf("idoso") }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    var pin by remember { mutableStateOf("") }
    var emailResponsavel by remember { mutableStateOf("") }
    var salvandoIdoso by remember { mutableStateOf(false) }

    val destinoCadastro: () -> Unit = when (perfilSelecionado) {
        "responsavel" -> onEntrarResponsavel
        "cuidador" -> onEntrarCuidador
        else -> onEntrarResponsavel
    }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Azul,
        unfocusedTextColor = Azul,
        cursorColor = Verde,
        focusedBorderColor = Verde,
        unfocusedBorderColor = Borda,
        focusedLabelColor = Verde,
        unfocusedLabelColor = Texto,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    LaunchedEffect(authMessage) {
        authMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            authViewModel.limparMensagem()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_voltar),
                contentDescription = "Voltar",
                modifier = Modifier.size(52.dp).clickable { onVoltar() },
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Criar conta", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
            Text("Escolha seu perfil e preencha seus dados.", color = Texto, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))

            Text("Eu sou:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "idoso" to "Idoso",
                    "responsavel" to "Responsável",
                    "cuidador" to "Cuidador"
                ).forEach { (valor, rotulo) ->
                    val selecionado = perfilSelecionado == valor
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable { perfilSelecionado = valor },
                        shape = RoundedCornerShape(14.dp),
                        color = if (selecionado) Verde else Color.White,
                        border = BorderStroke(1.5.dp, if (selecionado) Verde else Borda)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = rotulo,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selecionado) Color.White else AzulPrincipal
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                label = { Text("Nome completo") },
                placeholder = { Text("Digite seu nome", color = Texto) },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = campoColors
            )

            if (perfilSelecionado == "idoso") {
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) pin = it },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    label = { Text("PIN exclusivo de acesso (4 números)") },
                    placeholder = { Text("Ex: 1234", color = Texto) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Next),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(18.dp),
                    colors = campoColors
                )

                OutlinedTextField(
                    value = emailResponsavel,
                    onValueChange = { emailResponsavel = it },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    label = { Text("E-mail do Responsável") },
                    placeholder = { Text("responsavel@exemplo.com", color = Texto) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(18.dp),
                    colors = campoColors
                )
            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    label = { Text("E-mail") },
                    placeholder = { Text("seuemail@exemplo.com", color = Texto) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    shape = RoundedCornerShape(18.dp),
                    colors = campoColors
                )

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    label = { Text("Senha") },
                    placeholder = { Text("Crie uma senha (mín. 6 caracteres)", color = Texto) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(18.dp),
                    colors = campoColors
                )

                OutlinedTextField(
                    value = confirmar,
                    onValueChange = { confirmar = it },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    label = { Text("Confirmar senha") },
                    placeholder = { Text("Digite a senha novamente", color = Texto) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(18.dp),
                    colors = campoColors
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    when (perfilSelecionado) {
                        "idoso" -> {
                            if (nome.isBlank()) {
                                Toast.makeText(context, "Digite seu nome completo.", Toast.LENGTH_SHORT).show()
                            } else if (pin.length < 4) {
                                Toast.makeText(context, "O PIN precisa ter 4 números.", Toast.LENGTH_SHORT).show()
                            } else if (emailResponsavel.isBlank()) {
                                Toast.makeText(context, "Informe o e-mail do seu responsável.", Toast.LENGTH_SHORT).show()
                            } else {
                                salvandoIdoso = true
                                firestoreRepo.verificarPinExiste(pin) { jaExiste ->
                                    if (jaExiste) {
                                        salvandoIdoso = false
                                        Toast.makeText(context, "Este PIN já está sendo utilizado. Escolha outros 4 números.", Toast.LENGTH_LONG).show()
                                    } else {
                                        val ref = FirebaseDatabase.getInstance().getReference("usuarios").push()
                                        val dadosIdoso = mapOf(
                                            "nome" to nome,
                                            "pin" to pin,
                                            "emailResponsavel" to emailResponsavel,
                                            "tipo" to "idoso",
                                            "cadastroCompleto" to false
                                        )

                                        ref.setValue(dadosIdoso)
                                            .addOnSuccessListener {
                                                salvandoIdoso = false
                                                val prefs = context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE)
                                                prefs.edit {
                                                    putString("id_idoso", ref.key ?: "")
                                                    putString("nome_idoso", nome)
                                                    putString("email_responsavel", emailResponsavel)
                                                }
                                                Toast.makeText(context, "Cadastro realizado! Seu responsável completará sua ficha.", Toast.LENGTH_LONG).show()
                                                onEntrarIdoso()
                                            }
                                            .addOnFailureListener { e ->
                                                salvandoIdoso = false
                                                Toast.makeText(context, "Erro ao salvar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                            }
                                    }
                                }
                            }
                        }
                        else -> {
                            if (senha != confirmar) {
                                Toast.makeText(context, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                            } else {
                                authViewModel.cadastrar(email, senha, destinoCadastro)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp),
                enabled = !isLoading && !salvandoIdoso,
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                if (isLoading || salvandoIdoso) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp))
                } else {
                    Text("Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            if (perfilSelecionado != "idoso") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Borda)
                    Text("  ou  ", color = Texto, fontSize = 14.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Borda)
                }

                GoogleSignInButton(
                    texto = "Cadastrar com o Google",
                    isLoading = isLoading,
                    onClick = {
                        coroutineScope.launch {
                            iniciarFluxoGoogle(context, authViewModel, destinoCadastro)
                        }
                    }
                )
            }
        }
    }
}

// ============================================================
// AUXILIARES GOOGLE
// ============================================================

@Composable
private fun GoogleSignInButton(
    texto: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(18.dp),
        enabled = !isLoading,
        border = BorderStroke(1.dp, Borda),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = "G", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = texto, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AzulPrincipal)
        }
    }
}

private suspend fun iniciarFluxoGoogle(
    context: Context,
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit
) {
    try {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            authViewModel.loginComGoogle(googleIdTokenCredential.idToken, onSuccess)
        } else {
            Toast.makeText(context, "Credencial do Google inválida.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        if (e.javaClass.simpleName != "GetCredentialCancellationException") {
            Toast.makeText(context, "Erro no Google: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}