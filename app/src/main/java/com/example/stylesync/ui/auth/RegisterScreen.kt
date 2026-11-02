package com.example.stylesync.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.AppTextField
import com.example.stylesync.ui.components.AppTopBar
import com.example.stylesync.ui.components.PasswordField
import com.example.stylesync.ui.components.PrimaryButton

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(topBar = { AppTopBar(stringResource(R.string.auth_register_title), onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = stringResource(R.string.auth_name),
                errorText = viewModel.nameError?.let { stringResource(it) }
            )
            AppTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = stringResource(R.string.auth_email),
                errorText = viewModel.emailError?.let { stringResource(it) },
                keyboardType = KeyboardType.Email
            )
            PasswordField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = stringResource(R.string.auth_password),
                errorText = viewModel.passwordError?.let { stringResource(it) }
            )
            PasswordField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.confirmPassword = it },
                label = stringResource(R.string.auth_confirm_password),
                errorText = viewModel.confirmError?.let { stringResource(it) },
                imeAction = ImeAction.Done
            )
            PrimaryButton(
                text = stringResource(R.string.auth_register_button),
                onClick = { viewModel.register(onRegistered) },
                loading = viewModel.loading
            )
            TextButton(onClick = onBack) { Text(stringResource(R.string.auth_have_account)) }
        }
    }
}