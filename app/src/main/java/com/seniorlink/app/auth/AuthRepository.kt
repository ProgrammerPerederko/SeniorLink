package com.seniorlink.app.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, senha: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, senha).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuário retornado é nulo."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro no login por e-mail", e)
            Result.failure(e)
        }
    }

    suspend fun cadastrar(email: String, senha: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, senha).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Erro ao criar o usuário."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro no cadastro", e)
            Result.failure(e)
        }
    }

    suspend fun loginComGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuário do Google retornado é nulo."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "ERRO DO FIREBASE NO LOGIN COM GOOGLE: ${e.localizedMessage}", e)
            Result.failure(e)
        }
    }

    // ============================================================
    // RECUPERAÇÃO DE SENHA POR E-MAIL
    // ============================================================
    suspend fun recuperarSenha(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao enviar e-mail de recuperação", e)
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}