package com.seniorlink.app.data

/**
 * Repositório central do Firebase Realtime Database usado pelo SeniorLink.
 *
 * Apesar do nome histórico do arquivo, esta classe não usa Cloud Firestore.
 * Ela concentra as leituras e escritas do Realtime Database para perfis,
 * vínculos atuais, lembretes, contatos, ranking e emergências.
 *
 * A autorização real deve ser aplicada pelas Firebase Security Rules.
 * Este código também evita chamadas de gravação sem UID autenticado.
 */

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class JogadorFirestore(
    val uid: String = "",
    val nome: String = "",
    val avatar: String = "🌟",
    val pontos: Int = 0
)

data class LembreteFirestore(
    val id: String = "",
    val titulo: String = "",
    val horario: String = "",
    val categoria: String = "Remédio",
    val concluido: Boolean = false
)

data class ContatoFirestore(
    val id: String = "",
    val nome: String = "",
    val parentesco: String = "",
    val telefone: String = ""
)

data class PerfilVinculadoFirestore(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val tipo: String = "",
    // Informações aprofundadas do Idoso
    val idade: String = "",
    val tipoSanguineo: String = "",
    val condicoesMedicas: String = "",
    val alergias: String = "",
    val medicamentos: String = "",
    val endereco: String = "",
    val cadastroCompleto: Boolean = false
)

class FirestoreRepository {
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val uidAtual: String?
        get() = auth.currentUser?.uid

    private fun usuarioAutenticado(): String? =
        uidAtual?.takeIf { it.isNotBlank() }

    private val nomeAtual: String
        get() = auth.currentUser?.displayName?.takeIf { it.isNotBlank() } ?: "Usuário"

    // ============================================================
    // VALIDAÇÃO DE PIN ÚNICO E LOGIN DO IDOSO
    // ============================================================

    fun verificarPinExiste(pin: String, onResult: (Boolean) -> Unit) {
        db.getReference("usuarios")
            .orderByChild("pin")
            .equalTo(pin)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onResult(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(false)
                }
            })
    }

    fun buscarIdosoPorPin(pin: String, onResult: (PerfilVinculadoFirestore?) -> Unit) {
        db.getReference("usuarios")
            .orderByChild("pin")
            .equalTo(pin)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val doc = snapshot.children.firstOrNull {
                        it.child("tipo").getValue(String::class.java) == "idoso"
                    }
                    if (doc != null) {
                        onResult(mapearIdoso(doc))
                    } else {
                        onResult(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    // ============================================================
    // OBSERVAR E ATUALIZAR DADOS DETALHADOS DOS IDOSOS
    // ============================================================

    /**
     * Observa os idosos vinculados ao e-mail autenticado do responsável.
     *
     * O parâmetro existe por compatibilidade com as telas atuais, mas só é
     * aceito quando coincide com o e-mail da sessão Firebase.
     * O modelo definitivo deverá substituir o vínculo por e-mail por UID.
     */
    fun observarIdososDoResponsavel(emailResponsavel: String): Flow<List<PerfilVinculadoFirestore>> = callbackFlow {
        val emailAutenticado = auth.currentUser?.email?.trim().orEmpty()
        val emailSolicitado = emailResponsavel.trim()

        if (emailAutenticado.isBlank() ||
            emailSolicitado.isBlank() ||
            !emailAutenticado.equals(emailSolicitado, ignoreCase = true)
        ) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = db.getReference("usuarios")
            .orderByChild("emailResponsavel")
            .equalTo(emailAutenticado)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { doc ->
                    if (doc.child("tipo").getValue(String::class.java) == "idoso") {
                        mapearIdoso(doc)
                    } else {
                        null
                    }
                }
                trySend(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun atualizarFichaCompletaIdoso(
        idosoId: String,
        idade: String,
        tipoSanguineo: String,
        condicoesMedicas: String,
        alergias: String,
        medicamentos: String,
        endereco: String,
        telefone: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (idosoId.isBlank()) {
            onError("Identificador do idoso não informado.")
            return
        }

        if (usuarioAutenticado() == null) {
            onError("É necessário estar autenticado para atualizar a ficha.")
            return
        }

        val atualizacoes = mapOf(
            "idade" to idade,
            "tipoSanguineo" to tipoSanguineo,
            "condicoesMedicas" to condicoesMedicas,
            "alergias" to alergias,
            "medicamentos" to medicamentos,
            "endereco" to endereco,
            "telefone" to telefone,
            "cadastroCompleto" to true
        )

        db.getReference("usuarios").child(idosoId)
            .updateChildren(atualizacoes)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.localizedMessage ?: "Erro desconhecido") }
    }

    private fun mapearIdoso(doc: DataSnapshot): PerfilVinculadoFirestore {
        return PerfilVinculadoFirestore(
            id = doc.key ?: "",
            nome = doc.child("nome").getValue(String::class.java) ?: "",
            email = doc.child("emailResponsavel").getValue(String::class.java) ?: "",
            telefone = doc.child("telefone").getValue(String::class.java) ?: "",
            tipo = "idoso",
            idade = doc.child("idade").getValue(String::class.java) ?: "",
            tipoSanguineo = doc.child("tipoSanguineo").getValue(String::class.java) ?: "",
            condicoesMedicas = doc.child("condicoesMedicas").getValue(String::class.java) ?: "",
            alergias = doc.child("alergias").getValue(String::class.java) ?: "",
            medicamentos = doc.child("medicamentos").getValue(String::class.java) ?: "",
            endereco = doc.child("endereco").getValue(String::class.java) ?: "",
            cadastroCompleto = doc.child("cadastroCompleto").getValue(Boolean::class.java) ?: false
        )
    }

    // ============================================================
    // RANKING, LEMBRETES E CONTATOS (PADRÃO / IDOSO ATUAL)
    // ============================================================

    fun observarRanking(): Flow<List<JogadorFirestore>> = callbackFlow {
        val ref = db.getReference("ranking_idosos")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { doc ->
                    doc.getValue(JogadorFirestore::class.java)?.copy(uid = doc.key ?: "")
                }.sortedByDescending { it.pontos }.take(20)
                trySend(lista)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun adicionarPontos(pontosGanhos: Int) {
        val uid = usuarioAutenticado() ?: return
        if (pontosGanhos <= 0) return

        try {
            val ref = db.getReference("ranking_idosos").child(uid)
            val snapshot = ref.get().await()
            val atual = snapshot.getValue(JogadorFirestore::class.java)
            val novosPontos = (atual?.pontos ?: 0) + pontosGanhos
            ref.setValue(mapOf("nome" to nomeAtual, "avatar" to "🌟", "pontos" to novosPontos)).await()
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun observarLembretes(): Flow<List<LembreteFirestore>> = callbackFlow {
        val uid = usuarioAutenticado()
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = db.getReference("usuarios").child(uid).child("lembretes")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { doc ->
                    doc.getValue(LembreteFirestore::class.java)?.copy(id = doc.key ?: "")
                }.sortedBy { it.horario }
                trySend(lista)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun salvarLembrete(titulo: String, horario: String, categoria: String = "Remédio") {
        val uid = usuarioAutenticado() ?: return
        val ref = db.getReference("usuarios").child(uid).child("lembretes").push()
        ref.setValue(mapOf("titulo" to titulo, "horario" to horario, "categoria" to categoria, "concluido" to false)).await()
    }

    suspend fun alternarConcluidoLembrete(idLembrete: String, concluidoAtual: Boolean) {
        val uid = usuarioAutenticado() ?: return
        if (idLembrete.isBlank()) return

        db.getReference("usuarios")
            .child(uid)
            .child("lembretes")
            .child(idLembrete)
            .child("concluido")
            .setValue(!concluidoAtual)
            .await()
    }

    suspend fun excluirLembrete(idLembrete: String) {
        val uid = usuarioAutenticado() ?: return
        if (idLembrete.isBlank()) return

        db.getReference("usuarios")
            .child(uid)
            .child("lembretes")
            .child(idLembrete)
            .removeValue()
            .await()
    }

    // ============================================================
    // LEMBRETES ISOLADOS POR IDOSO (SUPORTE AO PAINEL DO RESPONSÁVEL)
    // ============================================================

    fun observarLembretesPorIdoso(idosoId: String): Flow<List<LembreteFirestore>> = callbackFlow {
        if (idosoId.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val ref = db.getReference("usuarios").child(idosoId).child("lembretes")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { doc ->
                    doc.getValue(LembreteFirestore::class.java)?.copy(id = doc.key ?: "")
                }.sortedBy { it.horario }
                trySend(lista)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun salvarLembretePorIdoso(idosoId: String, titulo: String, horario: String, categoria: String) {
        if (idosoId.isBlank()) return
        val ref = db.getReference("usuarios").child(idosoId).child("lembretes").push()
        ref.setValue(mapOf("titulo" to titulo, "horario" to horario, "categoria" to categoria, "concluido" to false)).await()
    }

    suspend fun alternarConcluidoLembretePorIdoso(idosoId: String, lembreteId: String, concluidoAtual: Boolean) {
        if (idosoId.isBlank() || lembreteId.isBlank()) return
        db.getReference("usuarios").child(idosoId).child("lembretes").child(lembreteId).child("concluido").setValue(!concluidoAtual).await()
    }

    suspend fun excluirLembretePorIdoso(idosoId: String, lembreteId: String) {
        if (idosoId.isBlank() || lembreteId.isBlank()) return
        db.getReference("usuarios").child(idosoId).child("lembretes").child(lembreteId).removeValue().await()
    }

    // ============================================================
    // CONTATOS
    // ============================================================

    fun observarContatos(): Flow<List<ContatoFirestore>> = callbackFlow {
        val uid = usuarioAutenticado()
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = db.getReference("usuarios").child(uid).child("contatos")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { doc ->
                    doc.getValue(ContatoFirestore::class.java)?.copy(id = doc.key ?: "")
                }
                trySend(lista)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun salvarContato(nome: String, parentesco: String, telefone: String) {
        val uid = usuarioAutenticado() ?: return
        if (nome.isBlank() || telefone.isBlank()) return

        val ref = db.getReference("usuarios").child(uid).child("contatos").push()
        ref.setValue(
            mapOf(
                "nome" to nome.trim(),
                "parentesco" to parentesco.trim(),
                "telefone" to telefone.trim()
            )
        ).await()
    }

    suspend fun excluirContato(idContato: String) {
        val uid = usuarioAutenticado() ?: return
        if (idContato.isBlank()) return

        db.getReference("usuarios")
            .child(uid)
            .child("contatos")
            .child(idContato)
            .removeValue()
            .await()
    }

    // ============================================================
    // EMERGÊNCIA EM TEMPO REAL (ATUALIZADO PARA DUPLO DESTINO)
    // ============================================================

    fun dispararEmergencia(
        idosoId: String,
        nomeIdoso: String,
        latitude: Double?,
        longitude: Double?,
        onSuccess: () -> Unit = {}
    ) {
        if (idosoId.isBlank()) return

        val alerta = hashMapOf<String, Any?>(
            "ativo" to true,
            "idosoId" to idosoId,
            "nomeIdoso" to nomeIdoso,
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to System.currentTimeMillis()
        )

        // 1. Grava no nó principal de emergências raiz
        db.getReference("emergencias")
            .child(idosoId)
            .setValue(alerta)

        // 2. Grava dentro do próprio perfil do usuário no nó de emergência para abranger qualquer escuta do app do responsável
        db.getReference("usuarios")
            .child(idosoId)
            .child("emergencia_ativa")
            .setValue(alerta)
            .addOnSuccessListener { onSuccess() }
    }

    fun encerrarEmergencia(idosoId: String) {
        if (idosoId.isBlank()) return

        // Desativa em ambos os locais
        db.getReference("emergencias")
            .child(idosoId)
            .child("ativo")
            .setValue(false)

        db.getReference("usuarios")
            .child(idosoId)
            .child("emergencia_ativa")
            .child("ativo")
            .setValue(false)
    }
}