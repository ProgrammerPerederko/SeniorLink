package com.seniorlink.app.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _authMessage = MutableStateFlow<String?>(null)
    val authMessage: StateFlow<String?> = _authMessage.asStateFlow()

    fun limparMensagem() {
        _authMessage.value = null
    }

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun login(email: String, senha: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(email, senha)
            _isLoading.value = false
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e -> _authMessage.value = "Erro no login: ${e.localizedMessage}" }
            )
        }
    }

    fun cadastrar(email: String, senha: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.cadastrar(email, senha)
            _isLoading.value = false
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e -> _authMessage.value = "Erro no cadastro: ${e.localizedMessage}" }
            )
        }
    }

    fun loginComGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.loginComGoogle(idToken)
            _isLoading.value = false
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e -> _authMessage.value = "Erro no login com Google: ${e.localizedMessage}" }
            )
        }
    }

    // ============================================================
    // RECUPERAÇÃO DE SENHA (CONECTADO AO REPOSITORY)
    // ============================================================
    fun recuperarSenha(email: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repository.recuperarSenha(email)
            _isLoading.value = false

            resultado.fold(
                onSuccess = {
                    onResult("E-mail de recuperação enviado com sucesso! Verifique sua caixa de entrada.")
                },
                onFailure = { error ->
                    onResult("Erro ao enviar e-mail: ${error.localizedMessage}")
                }
            )
        }
    }

    fun logout() {
        repository.logout()
    }
}