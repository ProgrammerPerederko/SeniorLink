
package com.seniorlink.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(onVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var sangue by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var condicoes by remember { mutableStateOf("") }
    var contato by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BotaoVoltar(onVoltar)

        TituloTela("Meu perfil")
        Text("Informações importantes para sua segurança.")

        Campo("Nome completo", nome) { nome = it }
        Campo("Data de nascimento", nascimento) { nascimento = it }
        Campo("Tipo sanguíneo", sangue) { sangue = it }
        Campo("Alergias", alergias) { alergias = it }
        Campo("Medicamentos em uso", medicamentos) { medicamentos = it }
        Campo("Condições importantes", condicoes) { condicoes = it }
        Campo("Contato de confiança", contato) { contato = it }
        Campo("Telefone do contato", telefone) { telefone = it }

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar informações")
        }

        Text(
            "Protótipo: os dados ainda não são salvos.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LocalizacaoScreen(onVoltar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BotaoVoltar(onVoltar)
        TituloTela("Localização")

        Text(
            "Acompanhe sua localização e compartilhe " +
                    "com uma pessoa de confiança."
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("📍 Sua localização")
                Text("O GPS será conectado em uma próxima etapa.")
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ativar localização")
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Pessoa de confiança")
                Text("Você poderá configurar quem recebe sua localização.")
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Configurar contato")
                }
            }
        }

        Text(
            "Protótipo: ainda não acessa o GPS nem compartilha " +
                    "a localização em tempo real.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LembretesScreen(onVoltar: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var lembretes by remember {
        mutableStateOf(listOf<String>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BotaoVoltar(onVoltar)
        TituloTela("Lembretes")

        Text("Organize suas atividades do dia a dia.")

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("O que precisa lembrar?") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Horário (ex.: 08:00)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (titulo.isNotBlank()) {
                    val item = if (horario.isBlank()) {
                        titulo
                    } else {
                        "$horario — $titulo"
                    }
                    lembretes = lembretes + item
                    titulo = ""
                    horario = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar lembrete")
        }

        Text(
            "Meus lembretes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (lembretes.isEmpty()) {
            Text("Você ainda não adicionou lembretes.")
        }

        lembretes.forEachIndexed { index, item ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        item,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            lembretes = lembretes
                                .filterIndexed { i, _ -> i != index }
                        }
                    ) {
                        Text("Excluir")
                    }
                }
            }
        }

        Text(
            "Protótipo: os lembretes não geram notificações " +
                    "e são apagados ao fechar esta tela.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AprenderScreen(onVoltar: () -> Unit) {
    var aberto by remember { mutableStateOf(-1) }

    val tutoriais = listOf(
        "Como aumentar o tamanho das letras" to
                "Abra as Configurações do celular, procure Tela " +
                "e ajuste o tamanho da fonte.",
        "Como fazer uma ligação" to
                "Abra o aplicativo Telefone, escolha um contato " +
                "e toque no botão de ligar.",
        "Como usar o Wi-Fi" to
                "Abra Configurações, entre em Wi-Fi e selecione " +
                "uma rede conhecida.",
        "Como enviar uma mensagem" to
                "Abra o aplicativo de mensagens, escolha um contato, " +
                "escreva e toque em enviar."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BotaoVoltar(onVoltar)
        TituloTela("Aprender")

        Text("Aprenda a usar seu celular passo a passo.")

        tutoriais.forEachIndexed { index, tutorial ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        tutorial.first,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    if (aberto == index) {
                        Text(tutorial.second)
                    }

                    OutlinedButton(
                        onClick = {
                            aberto =
                                if (aberto == index) -1 else index
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (aberto == index)
                                "Fechar tutorial"
                            else
                                "Ver passo a passo"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmergenciaScreen(onVoltar: () -> Unit) {
    var confirmar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BotaoVoltar(onVoltar)
        TituloTela("Emergência")

        Text(
            "Esta área reúne opções para pedir ajuda."
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Precisa de ajuda?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Se estiver em perigo imediato, procure " +
                            "ajuda pelos serviços de emergência locais."
                )

                Button(
                    onClick = { confirmar = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Pedir ajuda")
                }
            }
        }

        if (confirmar) {
            AlertDialog(
                onDismissRequest = { confirmar = false },
                title = { Text("Pedido de ajuda") },
                text = {
                    Text(
                        "Este protótipo ainda não envia alertas " +
                                "nem realiza chamadas."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { confirmar = false }
                    ) {
                        Text("Entendi")
                    }
                }
            )
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Contato de confiança — configurar depois")
        }
    }
}

@Composable
private fun TituloTela(texto: String) {
    Text(
        text = texto,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun BotaoVoltar(onVoltar: () -> Unit) {
    TextButton(onClick = onVoltar) {
        Text("← Voltar")
    }
}

@Composable
private fun Campo(
    titulo: String,
    valor: String,
    aoAlterar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        label = { Text(titulo) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}