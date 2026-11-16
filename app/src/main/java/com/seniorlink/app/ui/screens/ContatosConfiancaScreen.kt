package com.seniorlink.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seniorlink.app.R
import com.seniorlink.app.model.SeniorLinkPrefs

private val AzulEscuroContato = Color(0xFF123B63)
private val AzulPrincipalContato = Color(0xFF1764A5)
private val VerdeContato = Color(0xFF079B91)
private val FundoContato = Color(0xFFF7F9FC)
private val TextoSuaveContato = Color(0xFF66788A)
private val BordaContato = Color(0xFFE1E8EF)

data class ContatoConfiancaItem(
    val id: String = "",
    val nome: String = "",
    val parentesco: String = "",
    val telefone: String = ""
)

@Composable
fun ContatosConfiancaScreen(
    onVoltar: () -> Unit,
    idosoId: String = ""
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("seniorlink_prefs", Context.MODE_PRIVATE) }
    val escala = SeniorLinkPrefs.escalaFonte

    val idAlvo = remember(idosoId) {
        idosoId.ifBlank { prefs.getString("id_idoso", "") ?: "" }
    }

    var contatos by remember { mutableStateOf<List<ContatoConfiancaItem>>(emptyList()) }
    var mostrarModalNovo by remember { mutableStateOf(false) }

    DisposableEffect(idAlvo) {
        if (idAlvo.isNotBlank()) {
            val ref = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(idAlvo)
                .child("contatos")

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    contatos = snapshot.children.mapNotNull { child ->
                        ContatoConfiancaItem(
                            id = child.key ?: "",
                            nome = child.child("nome").getValue(String::class.java) ?: "",
                            parentesco = child.child("parentesco").getValue(String::class.java) ?: "",
                            telefone = child.child("telefone").getValue(String::class.java) ?: ""
                        )
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            ref.addValueEventListener(listener)
            onDispose { ref.removeEventListener(listener) }
        } else {
            onDispose { }
        }
    }

    val campoVisivelColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF0A2E4D),
        unfocusedTextColor = Color(0xFF0A2E4D),
        disabledTextColor = Color(0xFF0A2E4D),
        focusedContainerColor = Color(0xFFF0F4F8),
        unfocusedContainerColor = Color(0xFFF0F4F8),
        cursorColor = VerdeContato,
        focusedBorderColor = VerdeContato,
        unfocusedBorderColor = AzulPrincipalContato,
        focusedLabelColor = VerdeContato,
        unfocusedLabelColor = AzulEscuroContato,
        selectionColors = TextSelectionColors(
            handleColor = VerdeContato,
            backgroundColor = VerdeContato.copy(alpha = 0.25f)
        )
    )

    val textoCampoVisivel = TextStyle(
        color = Color(0xFF0A2E4D),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoContato)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 22.dp, top = 42.dp, end = 22.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clickable { onVoltar() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_voltar),
                        contentDescription = "Voltar",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Contatos de Confiança",
                        fontSize = (24 * escala).sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuroContato
                    )
                    Text(
                        text = "Toque em ligar para falar rapidamente.",
                        fontSize = (14 * escala).sp,
                        color = TextoSuaveContato
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            if (contatos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📞", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Nenhum contato salvo",
                            fontSize = (19 * escala).sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuroContato
                        )
                        Text(
                            text = "Adicione familiares, médicos ou cuidadores.",
                            fontSize = (14 * escala).sp,
                            color = TextoSuaveContato
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ) {
                    items(contatos, key = { it.id }) { contato ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            shadowElevation = 2.dp,
                            border = BorderStroke(1.dp, BordaContato)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(52.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE6F6F4)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = contato.nome.firstOrNull()?.uppercase() ?: "C",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VerdeContato
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = contato.nome,
                                        fontSize = (18 * escala).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulEscuroContato
                                    )
                                    if (contato.parentesco.isNotBlank()) {
                                        Text(
                                            text = contato.parentesco,
                                            fontSize = (13 * escala).sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = VerdeContato
                                        )
                                    }
                                    Text(
                                        text = contato.telefone,
                                        fontSize = (15 * escala).sp,
                                        color = TextoSuaveContato
                                    )
                                }

                                Button(
                                    onClick = {
                                        val uri = Uri.parse("tel:${contato.telefone}")
                                        context.startActivity(Intent(Intent.ACTION_DIAL, uri))
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = VerdeContato)
                                ) {
                                    Text("Ligar", color = Color.White, fontWeight = FontWeight.Bold)
                                }

                                IconButton(
                                    onClick = {
                                        if (idAlvo.isNotBlank() && contato.id.isNotBlank()) {
                                            FirebaseDatabase.getInstance()
                                                .getReference("usuarios")
                                                .child(idAlvo)
                                                .child("contatos")
                                                .child(contato.id)
                                                .removeValue()
                                        }
                                    }
                                ) {
                                    Text("🗑️", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { mostrarModalNovo = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp)
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeContato),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "+ Adicionar Contato",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (mostrarModalNovo) {
            var nomeNovo by remember { mutableStateOf("") }
            var parentescoNovo by remember { mutableStateOf("") }
            var telefoneNovo by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { mostrarModalNovo = false },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                title = {
                    Text(
                        text = "Novo Contato",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulEscuroContato
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = nomeNovo,
                            onValueChange = { nomeNovo = it },
                            label = { Text("Nome completo", fontWeight = FontWeight.Bold) },
                            placeholder = { Text("Ex: Dra. Ana / Filho Carlos", color = TextoSuaveContato) },
                            textStyle = textoCampoVisivel,
                            colors = campoVisivelColors,
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = parentescoNovo,
                            onValueChange = { parentescoNovo = it },
                            label = { Text("Parentesco ou Função", fontWeight = FontWeight.Bold) },
                            placeholder = { Text("Ex: Filho, Médico, Vizinha", color = TextoSuaveContato) },
                            textStyle = textoCampoVisivel,
                            colors = campoVisivelColors,
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = telefoneNovo,
                            onValueChange = { telefoneNovo = it },
                            label = { Text("Telefone com DDD", fontWeight = FontWeight.Bold) },
                            placeholder = { Text("Ex: (11) 99999-9999", color = TextoSuaveContato) },
                            textStyle = textoCampoVisivel,
                            colors = campoVisivelColors,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nomeNovo.isBlank() || telefoneNovo.isBlank()) {
                                Toast.makeText(context, "Preencha nome e telefone.", Toast.LENGTH_SHORT).show()
                            } else if (idAlvo.isBlank()) {
                                Toast.makeText(context, "Nenhum idoso vinculado encontrado.", Toast.LENGTH_SHORT).show()
                            } else {
                                val ref = FirebaseDatabase.getInstance()
                                    .getReference("usuarios")
                                    .child(idAlvo)
                                    .child("contatos")
                                    .push()

                                val dados = mapOf(
                                    "nome" to nomeNovo.trim(),
                                    "parentesco" to parentescoNovo.trim(),
                                    "telefone" to telefoneNovo.trim()
                                )
                                ref.setValue(dados).addOnSuccessListener {
                                    Toast.makeText(context, "Contato salvo!", Toast.LENGTH_SHORT).show()
                                    mostrarModalNovo = false
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeContato)
                    ) {
                        Text("Salvar Contato", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarModalNovo = false }) {
                        Text("Cancelar", color = TextoSuaveContato)
                    }
                }
            )
        }
    }
}