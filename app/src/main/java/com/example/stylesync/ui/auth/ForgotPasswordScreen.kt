package com.example.stylesync.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.AppTextField
import com.example.stylesync.ui.components.AppTopBar
import com.example.stylesync.ui.components.PrimaryButton

@Composable
fun ForgotPasswordScreen(viewModel: AuthViewModel, onBack: () -> Unit) {
    Scaffold(topBar = { AppTopBar(stringResource(R.string.auth_forgot_title), onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.auth_forgot_hint), style = MaterialTheme.typography.bodyLarge)
            AppTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = stringResource(R.string.auth_email),
                errorText = viewModel.emailError?.let { stringResource(it) },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
            viewModel.infoMessage?.let {
                Text(stringResource(it), color = MaterialTheme.colorScheme.primary)
            }
            PrimaryButton(
                text = stringResource(R.string.auth_send_link),
                onClick = { viewModel.sendRecovery() },
                loading = viewModel.loading
            )
        }
    }
}