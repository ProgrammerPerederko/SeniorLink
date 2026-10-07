package com.seniorlink.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF123B63)
private val Verde = Color(0xFF159C8C)

@Composable
fun LoginSeniorLinkScreen(
    onEntrar: () -> Unit,
    onCriarConta: () -> Unit,
    onEsqueciSenha: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("SeniorLink", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Text("Bem-vindo de volta!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Azul)
        Spacer(Modifier.height(8.dp))
        Text("Faça login para acessar sua conta.", color = Color.Gray)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("E-mail") }, singleLine = true)
        OutlinedTextField(
            senha, { senha = it }, Modifier.fillMaxWidth().padding(top = 10.dp),
            label = { Text("Senha") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        TextButton(onClick = onEsqueciSenha, modifier = Modifier.fillMaxWidth()) {
            Text("Esqueceu sua senha?", color = Azul)
        }

        Button(
            onClick = onEntrar,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Verde)
        ) { Text("Entrar", fontSize = 17.sp) }

        Spacer(Modifier.height(14.dp))
        Text("ou", color = Color.Gray, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        OutlinedButton(onClick = onCriarConta, modifier = Modifier.fillMaxWidth()) {
            Text("Criar uma conta", color = Verde)
        }
    }
}

@Composable
fun CadastroSeniorLinkScreen(
    onVoltar: () -> Unit,
    onCadastrar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextButton(onClick = onVoltar) { Text("← Voltar", color = Azul) }
        Text("Criar conta", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Text("Preencha os dados para começar.", color = Color.Gray)

        OutlinedTextField(nome, { nome = it }, Modifier.fillMaxWidth(), label = { Text("Nome completo") })
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("E-mail") })
        OutlinedTextField(
            senha, { senha = it }, Modifier.fillMaxWidth(),
            label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            confirmar, { confirmar = it }, Modifier.fillMaxWidth(),
            label = { Text("Confirmar senha") }, visualTransformation = PasswordVisualTransformation()
        )

        Text("Você poderá definir depois se é idoso ou responsável.", color = Color.Gray, fontSize = 13.sp)

        Button(
            onClick = onCadastrar,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Verde)
        ) { Text("Cadastrar", fontSize = 17.sp) }
    }
}

@Composable
fun BoasVindasSeniorLinkScreen(onContinuar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("💚", fontSize = 76.sp)
        Spacer(Modifier.height(18.dp))
        Text("Tudo pronto!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Spacer(Modifier.height(8.dp))
        Text("Agora você faz parte do SeniorLink.", fontSize = 17.sp, color = Color.Gray)
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onContinuar,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Verde)
        ) { Text("Começar a usar", fontSize = 17.sp) }
    }
}
