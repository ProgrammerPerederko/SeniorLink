package com.seniorlink.app.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import com.google.firebase.database.FirebaseDatabase

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

/**
 * Estado local e persistência do perfil/preferências do SeniorLink.
 *
 * O armazenamento remoto do perfil usa Firebase Realtime Database, seguindo
 * o mesmo backend adotado pelo restante do aplicativo.
 */
object SeniorLinkPrefs {
    private const val PREFS_NAME = "seniorlink_prefs"
    private const val KEY_ESCALA_FONTE = "escala_fonte"
    private const val KEY_ALTO_CONTRASTE = "alto_contraste"
    private const val KEY_LEITURA_VOZ = "leitura_voz_ativa"

    private var prefsContext: Context? = null

    private var _escalaFonte by mutableFloatStateOf(1.0f)
    var escalaFonte: Float
        get() = _escalaFonte
        set(value) {
            _escalaFonte = value
            prefsContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                ?.edit { putFloat(KEY_ESCALA_FONTE, value) }
        }

    private var _altoContraste by mutableStateOf(false)
    var altoContraste: Boolean
        get() = _altoContraste
        set(value) {
            _altoContraste = value
            prefsContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                ?.edit { putBoolean(KEY_ALTO_CONTRASTE, value) }
        }

    private var _leituraVozAtiva by mutableStateOf(true)
    var leituraVozAtiva: Boolean
        get() = _leituraVozAtiva
        set(value) {
            _leituraVozAtiva = value
            prefsContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                ?.edit { putBoolean(KEY_LEITURA_VOZ, value) }
        }

    var perfilAtual by mutableStateOf(PerfilIdoso())
        private set

    fun inicializar(context: Context) {
        if (prefsContext != null) return

        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsContext = appContext

        _escalaFonte = prefs.getFloat(KEY_ESCALA_FONTE, 1.0f)
        _altoContraste = prefs.getBoolean(KEY_ALTO_CONTRASTE, false)
        _leituraVozAtiva = prefs.getBoolean(KEY_LEITURA_VOZ, true)
    }

    fun salvarPerfilNoRealtimeDatabase(
        perfil: PerfilIdoso,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = perfil.uid.trim()
        if (uid.isBlank()) {
            onError("ID do idoso inválido para salvamento.")
            return
        }

        val dados = mapOf(
            "nome" to perfil.nome.trim(),
            "idade" to perfil.idade.trim(),
            "tipoSanguineo" to perfil.tipoSanguineo.trim(),
            "alergias" to perfil.alergias.trim(),
            "condicoesMedicas" to perfil.condicoesMedicas.trim(),
            "telefone" to perfil.contatoEmergencia.trim(),
            "endereco" to perfil.endereco.trim()
        )

        FirebaseDatabase.getInstance()
            .getReference("usuarios")
            .child(uid)
            .updateChildren(dados)
            .addOnSuccessListener {
                perfilAtual = perfil
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Erro ao salvar perfil")
            }
    }

    fun carregarPerfilDoRealtimeDatabase(uid: String) {
        val id = uid.trim()
        if (id.isBlank() || id == "idoso_teste") return

        FirebaseDatabase.getInstance()
            .getReference("usuarios")
            .child(id)
            .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val nome = snapshot.child("nome").getValue(String::class.java).orEmpty()
                    if (nome.isBlank() && !snapshot.exists()) return

                    perfilAtual = PerfilIdoso(
                        uid = id,
                        nome = nome,
                        idade = snapshot.child("idade").getValue(String::class.java).orEmpty(),
                        tipoSanguineo = snapshot.child("tipoSanguineo").getValue(String::class.java).orEmpty(),
                        alergias = snapshot.child("alergias").getValue(String::class.java).orEmpty(),
                        condicoesMedicas = snapshot.child("condicoesMedicas").getValue(String::class.java).orEmpty(),
                        contatoEmergencia = snapshot.child("telefone").getValue(String::class.java).orEmpty(),
                        endereco = snapshot.child("endereco").getValue(String::class.java).orEmpty()
                    )
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    // A tela continua com os dados locais atuais.
                }
            })
    }
}
