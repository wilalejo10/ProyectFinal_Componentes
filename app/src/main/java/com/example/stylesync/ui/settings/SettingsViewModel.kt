package com.example.stylesync.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.stylesync.data.local.SessionManager

class SettingsViewModel(private val session: SessionManager) : ViewModel() {

    val userName: String = session.userName
    val userEmail: String = session.userEmail

    var notificationsEnabled by mutableStateOf(session.notificationsEnabled)
        private set
    var reminderHour by mutableStateOf(session.reminderHour)
        private set
    var reminderMinute by mutableStateOf(session.reminderMinute)
        private set

    fun onNotificationsChanged(enabled: Boolean) {
        notificationsEnabled = enabled
        session.notificationsEnabled = enabled
    }

    fun onReminderChanged(hour: Int, minute: Int) {
        reminderHour = hour
        reminderMinute = minute
        session.reminderHour = hour
        session.reminderMinute = minute
    }

    fun logout() = session.logout()
}