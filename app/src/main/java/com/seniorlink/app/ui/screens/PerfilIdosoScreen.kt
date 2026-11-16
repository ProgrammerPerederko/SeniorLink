package com.seniorlink.app.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.model.SeniorLinkPrefs

private val AzulPrincipal = Color(0xFF06466B)
private val VerdeMarca = Color(0xFF159C8C)
private val FundoCinza = Color(0xFFF6FAFB)
private val TextoSuave = Color(0xFF52677A)
private val BordaCard = Color(0xFFD7E3E8)
private val VerdeSuave = Color(0xFFE6F4F1)

@Composable
fun PerfilIdosoScreen(
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }

    val idosoId = remember {
        prefs.getString("id_idoso", null)
            ?: prefs.getString("idoso_id", null)
            ?: FirebaseAuth.getInstance().currentUser?.uid
            ?: ""
    }

    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var tipoSanguineo by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var condicoesMedicas by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }

    var carregando by remember { mutableStateOf(true) }
    var salvando by remember { mutableStateOf(false) }

    val escala = SeniorLinkPrefs.escalaFonte

    LaunchedEffect(idosoId) {
        if (idosoId.isNotBlank()) {
            FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nome = snapshot.child("nome").getValue(String::class.java) ?: ""
                        idade = snapshot.child("idade").getValue(String::class.java) ?: ""
                        tipoSanguineo = snapshot.child("tipoSanguineo").getValue(String::class.java) ?: ""
                        telefone = snapshot.child("telefone").getValue(String::class.java) ?: ""
                        endereco = snapshot.child("endereco").getValue(String::class.java) ?: ""
                        condicoesMedicas = snapshot.child("condicoesMedicas").getValue(String::class.java) ?: ""
                        medicamentos = snapshot.child("medicamentos").getValue(String::class.java) ?: ""
                        alergias = snapshot.child("alergias").getValue(String::class.java) ?: ""
                        carregando = false
                    }
                    override fun onCancelled(error: DatabaseError) {
                        carregando = false
                    }
                })
        } else {
            carregando = false
        }
    }

    val campoColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AzulPrincipal,
        unfocusedTextColor = AzulPrincipal,
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onVoltar() },
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, BordaCard)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Perfil do Idoso",
                        fontSize = (22 * escala).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulPrincipal
                    )
                    Text(
                        text = "Gerencie as informações pessoais e médicas",
                        fontSize = (13 * escala).sp,
                        color = TextoSuave
                    )
                }
            }

            if (carregando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VerdeMarca)
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BordaCard),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = VerdeSuave,
                            border = BorderStroke(1.5.dp, VerdeMarca.copy(alpha = 0.5f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val iniciais = remember(nome) {
                                    val partes = nome.trim().split(" ").filter { it.isNotBlank() }
                                    when {
                                        partes.isEmpty() -> "SL"
                                        partes.size == 1 -> partes[0].take(2).uppercase()
                                        else -> "${partes[0].take(1)}${partes.last().take(1)}".uppercase()
                                    }
                                }
                                Text(
                                    text = iniciais,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeMarca
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = nome.ifBlank { "Nome não informado" },
                                fontSize = (18 * escala).sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrincipal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Toque nos campos abaixo para editar",
                                fontSize = (12 * escala).sp,
                                color = TextoSuave
                            )
                        }
                    }
                }

                Text(text = "Dados Pessoais", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BordaCard),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome Completo *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )

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
                                label = { Text("Tipo Sanguíneo") },
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
                            label = { Text("Telefone / Contato") },
                            placeholder = { Text("(11) 99999-9999") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )

                        OutlinedTextField(
                            value = endereco,
                            onValueChange = { endereco = it },
                            label = { Text("Endereço Residencial") },
                            placeholder = { Text("Rua, número e bairro") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )
                    }
                }

                Text(text = "Histórico Médico", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BordaCard),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value = condicoesMedicas,
                            onValueChange = { condicoesMedicas = it },
                            label = { Text("Condições Médicas / Doenças Crônicas") },
                            placeholder = { Text("Ex: Hipertensão, Diabetes...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )

                        OutlinedTextField(
                            value = medicamentos,
                            onValueChange = { medicamentos = it },
                            label = { Text("Medicamentos em Uso") },
                            placeholder = { Text("Ex: Losartana 50mg (manhã)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = campoColors
                        )

                        OutlinedTextField(
                            value = alergias,
                            onValueChange = { alergias = it },
                            label = { Text("Alergias") },
                            placeholder = { Text("Ex: Dipirona ou Nenhuma") },
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
                        if (nome.isBlank()) {
                            Toast.makeText(context, "O nome não pode estar vazio.", Toast.LENGTH_SHORT).show()
                        } else {
                            salvando = true
                            val atualizacoes = mapOf(
                                "nome" to nome,
                                "idade" to idade,
                                "tipoSanguineo" to tipoSanguineo,
                                "telefone" to telefone,
                                "endereco" to endereco,
                                "condicoesMedicas" to condicoesMedicas,
                                "medicamentos" to medicamentos,
                                "alergias" to alergias
                            )

                            FirebaseDatabase.getInstance().getReference("usuarios").child(idosoId)
                                .updateChildren(atualizacoes)
                                .addOnSuccessListener {
                                    salvando = false
                                    prefs.edit().putString("nome_idoso", nome).apply()
                                    Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                                    onVoltar()
                                }
                                .addOnFailureListener { e ->
                                    salvando = false
                                    Toast.makeText(context, "Erro ao salvar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeMarca)
                ) {
                    if (salvando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Salvar Alterações",
                            fontSize = (16 * escala).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}