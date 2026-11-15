package com.seniorlink.app.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore

data class PerfilIdoso(
    val uid: String = "",
    val nome: String = "",
    val idade: String = "",
    val tipoSanguineo: String = "",
    val alergias: String = "",
    val condicoesMedicas: String = "",
    val contatoEmergencia: String = "",
    val endereco: String = ""
) {
    val inicial: String
        get() = nome.trim().firstOrNull()?.uppercase() ?: "SL"
}

object SeniorLinkPrefs {
    var escalaFonte by mutableFloatStateOf(1.0f)
    var altoContraste by mutableStateOf(false)
    var leituraVozAtiva by mutableStateOf(true)
    var perfilAtual by mutableStateOf(PerfilIdoso())

    fun salvarPerfilNoFirestore(perfil: PerfilIdoso, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        if (perfil.uid.isEmpty()) {
            onError("ID do idoso inválido para salvamento.")
            return
        }

        db.collection("idosos")
            .document(perfil.uid)
            .set(perfil)
            .addOnSuccessListener {
                perfilAtual = perfil
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Erro ao salvar perfil")
            }
    }

    fun carregarPerfilDoFirestore(uid: String) {
        if (uid.isBlank() || uid == "idoso_teste") return

        val db = FirebaseFirestore.getInstance()
        db.collection("idosos")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                doc.toObject(PerfilIdoso::class.java)?.let {
                    perfilAtual = it
                }
            }
    }
}