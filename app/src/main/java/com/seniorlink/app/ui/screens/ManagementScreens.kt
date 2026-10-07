package com.seniorlink.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF123B63)
private val Verde = Color(0xFF159C8C)
private val Vermelho = Color(0xFFE94B4B)

@Composable
fun NovoLembreteScreen(onVoltar: () -> Unit, onSalvar: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onVoltar) { Text("← Voltar", color = Azul) }
        Text("Novo lembrete", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Text("Crie um lembrete simples para ajudar na rotina.", color = Color.Gray)

        OutlinedTextField(titulo, { titulo = it }, Modifier.fillMaxWidth(), label = { Text("Título") })
        OutlinedTextField(descricao, { descricao = it }, Modifier.fillMaxWidth(), label = { Text("Descrição") })

        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Selecionar data") }
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Selecionar horário") }

        Text("Repetição", fontWeight = FontWeight.Bold, color = Azul)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Diariamente", modifier = Modifier.weight(1f))
            Switch(checked = false, onCheckedChange = {})
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onSalvar,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Verde)
        ) { Text("Salvar lembrete") }
    }
}

@Composable
fun ContatosConfiancaScreen(onVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onVoltar) { Text("← Voltar", color = Azul) }
        Text("Meus contatos", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Text("Pessoas que podem ajudar em uma emergência.", color = Color.Gray)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Contato de confiança", fontWeight = FontWeight.Bold, color = Azul)
                OutlinedTextField(nome, { nome = it }, Modifier.fillMaxWidth(), label = { Text("Nome") })
                OutlinedTextField(telefone, { telefone = it }, Modifier.fillMaxWidth(), label = { Text("Telefone") })
                Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Verde)) {
                    Text("Adicionar contato")
                }
            }
        }

        Text("Depois, essa área será ligada ao banco local/remoto e ao botão de emergência.", color = Color.Gray, fontSize = 13.sp)
    }
}

@Composable
fun ConfiguracoesSeniorLinkScreen(
    onVoltar: () -> Unit,
    onPerfil: () -> Unit,
    onContatos: () -> Unit,
    onTreinamentoResponsavel: () -> Unit,
    onLogout: () -> Unit
) {
    var notificacoes by remember { mutableStateOf(true) }
    var fonteMaior by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextButton(onClick = onVoltar) { Text("← Voltar", color = Azul) }
        Text("Configurações", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)

        OutlinedButton(onClick = onPerfil, modifier = Modifier.fillMaxWidth()) { Text("Minha conta") }
        OutlinedButton(onClick = onContatos, modifier = Modifier.fillMaxWidth()) { Text("Contatos de confiança") }
        OutlinedButton(onClick = onTreinamentoResponsavel, modifier = Modifier.fillMaxWidth()) {
            Text("Treinamento do responsável")
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Notificações", modifier = Modifier.weight(1f))
            Switch(notificacoes, { notificacoes = it })
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Texto maior", modifier = Modifier.weight(1f))
            Switch(fonteMaior, { fonteMaior = it })
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Vermelho)
        ) { Text("Sair da conta") }
    }
}

@Composable
fun TreinamentoResponsavelScreen(onVoltar: () -> Unit) {
    val passos = listOf(
        "1. Cadastre o idoso e revise os dados do perfil.",
        "2. Confirme quem são os contatos de confiança.",
        "3. Verifique se a localização e as permissões estão ativas.",
        "4. Ensine o idoso a usar lembretes e o botão de emergência.",
        "5. Em caso de problema, valide permissões e conexão antes de abrir chamado."
    )

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onVoltar) { Text("← Voltar", color = Azul) }
        Text("Treinamento do responsável", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Azul)
        Text("Orientações para acompanhar o idoso e resolver situações básicas.", color = Color.Gray)

        passos.forEach { passo ->
            Card(Modifier.fillMaxWidth()) {
                Text(passo, Modifier.padding(16.dp), color = Azul, fontSize = 16.sp)
            }
        }
    }
}
