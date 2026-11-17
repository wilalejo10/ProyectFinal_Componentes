package com.example.stylesync.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.stylesync.data.report.ReportFiles

@Composable
fun ReportStatusCard(state: ReportState, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var noApp by remember(state) { mutableStateOf(false) }

    when (state) {
        ReportState.Idle -> Unit
        ReportState.Working -> Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
            Text(stringResource(R.string.report_generating))
        }
        is ReportState.Success -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                stringResource(R.string.report_success, state.fileName),
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { noApp = !ReportFiles.open(context, state.uri, state.mime) }) {
                    Text(stringResource(R.string.report_open))
                }
                OutlinedButton(onClick = { ReportFiles.share(context, state.uri, state.mime) }) {
                    Text(stringResource(R.string.report_share))
                }
            }
            if (noApp) {
                Text(stringResource(R.string.report_no_app), color = MaterialTheme.colorScheme.error)
            }
        }
        is ReportState.Error -> Text(
            stringResource(state.message),
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}