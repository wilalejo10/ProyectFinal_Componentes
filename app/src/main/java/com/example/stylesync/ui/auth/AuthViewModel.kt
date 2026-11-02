package com.example.stylesync.ui.auth

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesync.R
import com.example.stylesync.data.local.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Un mismo ViewModel sirve a Login, Registro y Recuperar (cada pantalla tiene su propia instancia). */
class AuthViewModel(private val session: SessionManager) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var loading by mutableStateOf(false)
        private set

    @StringRes var nameError by mutableStateOf<Int?>(null)
    @StringRes var emailError by mutableStateOf<Int?>(null)
    @StringRes var passwordError by mutableStateOf<Int?>(null)
    @StringRes var confirmError by mutableStateOf<Int?>(null)
    @StringRes var formError by mutableStateOf<Int?>(null)
    @StringRes var infoMessage by mutableStateOf<Int?>(null)

    private fun clearErrors() {
        nameError = null; emailError = null; passwordError = null
        confirmError = null; formError = null; infoMessage = null
    }

    private fun validEmail() = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    fun login(onSuccess: () -> Unit) {
        clearErrors()
        var ok = true
        if (!validEmail()) { emailError = R.string.auth_error_email_invalid; ok = false }
        if (password.length < 6) { passwordError = R.string.auth_error_password_short; ok = false }
        if (!ok) return
        viewModelScope.launch {
            loading = true
            delay(700) // simula la llamada de red
            if (session.login(email, password)) onSuccess() else formError = R.string.auth_error_credentials
            loading = false
        }
    }

    fun register(onSuccess: () -> Unit) {
        clearErrors()
        var ok = true
        if (name.isBlank()) { nameError = R.string.auth_error_name_empty; ok = false }
        if (!validEmail()) { emailError = R.string.auth_error_email_invalid; ok = false }
        if (password.length < 6) { passwordError = R.string.auth_error_password_short; ok = false }
        if (confirmPassword != password) { confirmError = R.string.auth_error_password_mismatch; ok = false }
        if (!ok) return
        viewModelScope.launch {
            loading = true
            delay(700)
            if (session.register(name, email, password)) onSuccess() else emailError = R.string.auth_error_email_taken
            loading = false
        }
    }

    fun sendRecovery() {
        clearErrors()
        if (!validEmail()) { emailError = R.string.auth_error_email_invalid; return }
        viewModelScope.launch {
            loading = true
            delay(700)
            loading = false
            infoMessage = R.string.auth_recovery_sent // el envío real llega con Firebase en la Entrega 2
        }
    }
}