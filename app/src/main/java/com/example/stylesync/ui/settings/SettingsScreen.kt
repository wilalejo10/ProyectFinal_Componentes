package com.example.stylesync.ui.settings

import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stylesync.R
import com.example.stylesync.ui.components.PrimaryButton
import com.example.stylesync.ui.theme.components.ConfirmDialog

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val time = "%02d:%02d".format(viewModel.reminderHour, viewModel.reminderMinute)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.settings_account), style = MaterialTheme.typography.labelLarge)
        Text(viewModel.userName, style = MaterialTheme.typography.titleLarge)
        Text(viewModel.userEmail, style = MaterialTheme.typography.bodyMedium)

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.settings_notifications))
            Switch(
                checked = viewModel.notificationsEnabled,
                onCheckedChange = viewModel::onNotificationsChanged
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = viewModel.notificationsEnabled) {
                    TimePickerDialog(
                        context,
                        { _, hour, minute -> viewModel.onReminderChanged(hour, minute) },
                        viewModel.reminderHour,
                        viewModel.reminderMinute,
                        DateFormat.is24HourFormat(context)
                    ).show()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.settings_reminder_time),
                color = if (viewModel.notificationsEnabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.outline
            )
            Text(time, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.settings_logout),
            onClick = { showLogoutDialog = true }
        )
    }

    ConfirmDialog(
        showDialog = showLogoutDialog,
        title = stringResource(R.string.settings_logout),
        message = stringResource(R.string.settings_logout_confirm),
        onConfirm = {
            showLogoutDialog = false
            viewModel.logout()
            onLogout()
        },
        onDismiss = { showLogoutDialog = false }
    )
}