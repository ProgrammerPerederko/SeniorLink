
package com.seniorlink.app.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// CORES SENIORLINK
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
// NOVO LEMBRETE
// ============================================================

@Composable
fun NovoLembreteScreen(
    onVoltar: () -> Unit,
    onSalvar: () -> Unit
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    var dataSelecionada by remember {
        mutableStateOf("")
    }

    var horarioSelecionado by remember {
        mutableStateOf("")
    }

    var repetir by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 22.dp,
                vertical = 24.dp
            )
    ) {

        CabecalhoManagement(
            titulo = "Novo lembrete",
            subtitulo = "Crie um lembrete para sua rotina.",
            onVoltar = onVoltar
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ====================================================
        // DADOS DO LEMBRETE
        // ====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "O que você precisa lembrar?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )


                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Título")
                    },
                    placeholder = {
                        Text("Ex.: Tomar medicamento")
                    }
                )


                OutlinedTextField(
                    value = descricao,
                    onValueChange = {
                        descricao = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    label = {
                        Text("Descrição")
                    },
                    placeholder = {
                        Text("Ex.: Tomar após o almoço")
                    }
                )
            }
        }


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // ====================================================
        // DATA E HORÁRIO
        // ====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Quando?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )


                ManagementOption(
                    titulo =
                        if (dataSelecionada.isBlank()) {
                            "Selecionar data"
                        } else {
                            dataSelecionada
                        },
                    subtitulo =
                        "Escolha o dia do lembrete"
                ) {

                    dataSelecionada =
                        "Data selecionada"
                }


                ManagementOption(
                    titulo =
                        if (horarioSelecionado.isBlank()) {
                            "Selecionar horário"
                        } else {
                            horarioSelecionado
                        },
                    subtitulo =
                        "Escolha o horário"
                ) {

                    horarioSelecionado =
                        "Horário selecionado"
                }
            }
        }


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // ====================================================
        // REPETIÇÃO
        // ====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Repetir diariamente",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Receber este lembrete todos os dias.",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }


                Switch(
                    checked = repetir,
                    onCheckedChange = {
                        repetir = it
                    }
                )
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ====================================================
        // SALVAR
        // ====================================================

        Button(
            onClick = onSalvar,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {

            Text(
                text = "Salvar lembrete",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Text(
            text =
                "Depois vamos conectar os lembretes ao banco de dados e às notificações do celular.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )

        RodapeManagement()
    }
}


// ============================================================
// CONTATOS DE CONFIANÇA
// ============================================================

@Composable
fun ContatosConfiancaScreen(
    onVoltar: () -> Unit
) {

    var nome by remember {
        mutableStateOf("")
    }

    var telefone by remember {
        mutableStateOf("")
    }

    var parentesco by remember {
        mutableStateOf("")
    }

    val contatos =
        remember {
            mutableStateListOf<ContatoLocal>()
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 22.dp,
                vertical = 24.dp
            )
    ) {

        CabecalhoManagement(
            titulo = "Meus contatos",
            subtitulo =
                "Pessoas que podem ajudar você.",
            onVoltar = onVoltar
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        // ====================================================
        // NOVO CONTATO
        // ====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Adicionar contato",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )


                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Nome")
                    },
                    placeholder = {
                        Text("Ex.: João Silva")
                    }
                )


                OutlinedTextField(
                    value = parentesco,
                    onValueChange = {
                        parentesco = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Relação")
                    },
                    placeholder = {
                        Text("Ex.: Filho")
                    }
                )


                OutlinedTextField(
                    value = telefone,
                    onValueChange = {
                        telefone = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Telefone")
                    },
                    placeholder = {
                        Text("Ex.: (11) 99999-9999")
                    }
                )


                Spacer(
                    modifier = Modifier.height(4.dp)
                )


                Button(
                    onClick = {

                        if (
                            nome.isNotBlank() &&
                            telefone.isNotBlank()
                        ) {

                            contatos.add(
                                ContatoLocal(
                                    nome = nome,
                                    telefone = telefone,
                                    parentesco =
                                        parentesco.ifBlank {
                                            "Contato de confiança"
                                        }
                                )
                            )

                            nome = ""
                            telefone = ""
                            parentesco = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Verde
                    )
                ) {

                    Text(
                        text = "Adicionar contato",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ====================================================
        // LISTA
        // ====================================================

        Text(
            text = "Pessoas cadastradas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulEscuro
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        if (contatos.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AzulClaro
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Branco,
                                shape =
                                    RoundedCornerShape(18.dp)
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "+",
                            fontSize = 30.sp,
                            color = AzulPrincipal
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )


                    Text(
                        text =
                            "Nenhum contato cadastrado",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )


                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )


                    Text(
                        text =
                            "Adicione alguém de confiança para receber ajuda quando necessário.",
                        modifier =
                            Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {

            contatos.forEachIndexed { index, contato ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 10.dp
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Branco
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(
                                    color = AzulClaro,
                                    shape =
                                        RoundedCornerShape(
                                            18.dp
                                        )
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    contato.nome
                                        .first()
                                        .uppercase(),
                                fontSize = 22.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color = AzulPrincipal
                            )
                        }


                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )


                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = contato.nome,
                                fontSize = 17.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color = AzulEscuro
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(2.dp)
                            )

                            Text(
                                text =
                                    contato.parentesco,
                                fontSize = 13.sp,
                                color =
                                    TextoSecundario
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(2.dp)
                            )

                            Text(
                                text =
                                    contato.telefone,
                                fontSize = 13.sp,
                                color =
                                    AzulPrincipal
                            )
                        }


                        TextButton(
                            onClick = {
                                contatos.removeAt(index)
                            }
                        ) {

                            Text(
                                text = "Excluir",
                                color = Vermelho,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text =
                "Depois vamos conectar estes contatos ao Firebase e ao botão de emergência.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )

        RodapeManagement()
    }
}


// ============================================================
// CONFIGURAÇÕES
// ============================================================

@Composable
fun ConfiguracoesSeniorLinkScreen(
    onVoltar: () -> Unit,
    onPerfil: () -> Unit,
    onContatos: () -> Unit,
    onTreinamentoResponsavel: () -> Unit,
    onLogout: () -> Unit
) {

    var notificacoes by remember {
        mutableStateOf(true)
    }

    var fonteMaior by remember {
        mutableStateOf(false)
    }

    var mostrarSobre by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 22.dp,
                vertical = 24.dp
            )
    ) {

        CabecalhoManagement(
            titulo = "Configurações",
            subtitulo = "Personalize o SeniorLink.",
            onVoltar = onVoltar
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        // ====================================================
        // CONTA
        // ====================================================

        Text(
            text = "Minha conta",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = AzulEscuro
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        ManagementOption(
            titulo = "Meu perfil",
            subtitulo = "Dados e informações pessoais"
        ) {
            onPerfil()
        }


        ManagementOption(
            titulo = "Contatos de confiança",
            subtitulo = "Pessoas que podem ajudar"
        ) {
            onContatos()
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // ====================================================
        // PREFERÊNCIAS
        // ====================================================

        Text(
            text = "Preferências",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = AzulEscuro
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        SwitchOption(
            titulo = "Notificações",
            subtitulo =
                "Receber alertas e lembretes",
            valor = notificacoes,
            onChange = {
                notificacoes = it
            }
        )


        SwitchOption(
            titulo = "Texto maior",
            subtitulo =
                "Aumentar o tamanho dos textos",
            valor = fonteMaior,
            onChange = {
                fonteMaior = it
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // ====================================================
        // TREINAMENTO
        // ====================================================

        Text(
            text = "Ajuda",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = AzulEscuro
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        ManagementOption(
            titulo = "Treinamento do responsável",
            subtitulo =
                "Aprenda a acompanhar o idoso"
        ) {
            onTreinamentoResponsavel()
        }


        ManagementOption(
            titulo = "Sobre o SeniorLink",
            subtitulo =
                "Informações sobre o aplicativo"
        ) {
            mostrarSobre = true
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        // ====================================================
        // SAIR
        // ====================================================

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(18.dp)
        ) {

            Text(
                text = "Sair da conta",
                color = Vermelho,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )

        RodapeManagement()
    }


    // ========================================================
    // SOBRE
    // ========================================================

    if (mostrarSobre) {

        AlertDialog(
            onDismissRequest = {
                mostrarSobre = false
            },

            title = {
                Text(
                    text = "SeniorLink",
                    color = AzulPrincipal,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    text =
                        "Cuidado, autonomia e conexão.\n\n" +
                                "Aplicativo desenvolvido para " +
                                "auxiliar idosos e responsáveis " +
                                "no uso seguro do celular."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        mostrarSobre = false
                    }
                ) {

                    Text(
                        text = "Fechar",
                        color = AzulPrincipal
                    )
                }
            }
        )
    }
}


// ============================================================
// TREINAMENTO DO RESPONSÁVEL
// ============================================================

@Composable
fun TreinamentoResponsavelScreen(
    onVoltar: () -> Unit
) {

    val passos = listOf(

        "Cadastre o idoso e revise seus dados importantes.",

        "Confirme quem são os contatos de confiança.",

        "Verifique se a localização e as permissões estão ativas.",

        "Ensine o idoso a utilizar os lembretes e o botão de emergência.",

        "Em caso de problema, verifique primeiro permissões e conexão."
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 22.dp,
                vertical = 24.dp
            )
    ) {

        CabecalhoManagement(
            titulo = "Treinamento",
            subtitulo =
                "Orientações para o responsável.",
            onVoltar = onVoltar
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        Text(
            text =
                "Siga estas orientações para acompanhar " +
                        "o idoso com segurança.",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = Texto
        )


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        passos.forEachIndexed { index, passo ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Branco
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = AzulClaro,
                                shape =
                                    RoundedCornerShape(
                                        16.dp
                                    )
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "${index + 1}",
                            fontSize = 20.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color = AzulPrincipal
                        )
                    }


                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )


                    Text(
                        text = passo,
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = Texto
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdeClaro
            )
        ) {

            Text(
                text =
                    "O treinamento será ampliado conforme " +
                            "as funcionalidades do SeniorLink forem " +
                            "conectadas ao banco e aos serviços reais.",
                modifier = Modifier.padding(18.dp),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Texto
            )
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )

        RodapeManagement()
    }
}


// ============================================================
// CABEÇALHO
// ============================================================

@Composable
private fun CabecalhoManagement(
    titulo: String,
    subtitulo: String,
    onVoltar: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = Branco,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    onVoltar()
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "‹",
                fontSize = 34.sp,
                color = AzulPrincipal
            )
        }


        Spacer(
            modifier = Modifier.width(14.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitulo,
                fontSize = 14.sp,
                color = TextoSecundario
            )
        }
    }
}


// ============================================================
// OPÇÃO
// ============================================================

@Composable
private fun ManagementOption(
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            )
            .background(
                color = Branco,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = AzulClaro,
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "›",
                fontSize = 30.sp,
                color = AzulPrincipal
            )
        }


        Spacer(
            modifier = Modifier.width(14.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitulo,
                fontSize = 12.sp,
                color = TextoSecundario
            )
        }


        Text(
            text = "›",
            fontSize = 30.sp,
            color = AzulPrincipal
        )
    }
}


// ============================================================
// SWITCH
// ============================================================

@Composable
private fun SwitchOption(
    titulo: String,
    subtitulo: String,
    valor: Boolean,
    onChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Branco,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitulo,
                fontSize = 12.sp,
                color = TextoSecundario
            )
        }


        Switch(
            checked = valor,
            onCheckedChange = onChange
        )
    }


    Spacer(
        modifier = Modifier.height(7.dp)
    )
}


// ============================================================
// CONTATO LOCAL
// ============================================================

private data class ContatoLocal(
    val nome: String,
    val telefone: String,
    val parentesco: String
)


// ============================================================
// RODAPÉ
// ============================================================

@Composable
private fun RodapeManagement() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalDivider(
            color = Borda,
            thickness = 1.dp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "SeniorLink",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrincipal
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "Cuidado, autonomia e conexão",
            fontSize = 11.sp,
            color = TextoSecundario
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )
    }
}
