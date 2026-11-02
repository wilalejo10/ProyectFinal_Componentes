package com.example.stylesync.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.example.stylesync.ui.components.ErrorMessage
import com.example.stylesync.ui.components.PasswordField
import com.example.stylesync.ui.components.PrimaryButton

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoggedIn: () -> Unit,
    onRegister: () -> Unit,
    onForgot: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(stringResource(R.string.auth_login_title), style = MaterialTheme.typography.titleLarge)

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
            errorText = viewModel.passwordError?.let { stringResource(it) },
            imeAction = ImeAction.Done
        )
        viewModel.formError?.let { ErrorMessage(stringResource(it)) }

        PrimaryButton(
            text = stringResource(R.string.auth_login_button),
            onClick = { viewModel.login(onLoggedIn) },
            loading = viewModel.loading
        )
        TextButton(onClick = onForgot) { Text(stringResource(R.string.auth_forgot_link)) }
        TextButton(onClick = onRegister) { Text(stringResource(R.string.auth_no_account)) }
    }
}