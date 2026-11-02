package com.example.stylesync.data.local

import android.content.Context

/**
 * Login FALSO y local para la Entrega 1 (sin Firebase todavía).
 * Guarda cuentas y ajustes en SharedPreferences.
 * OJO: las contraseñas quedan en texto plano; en la Entrega 2 esto se reemplaza por Firebase Auth.
 *
 * Cuenta de demo siempre disponible: demo@stylesync.com / 123456
 */
class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("stylesync_session", Context.MODE_PRIVATE)

    val isLoggedIn: Boolean get() = prefs.getBoolean(KEY_LOGGED, false)
    val userName: String get() = prefs.getString(KEY_NAME, "") ?: ""
    val userEmail: String get() = prefs.getString(KEY_EMAIL, "") ?: ""

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIF, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIF, value).apply()

    var reminderHour: Int
        get() = prefs.getInt(KEY_HOUR, 8)
        set(value) = prefs.edit().putInt(KEY_HOUR, value).apply()

    var reminderMinute: Int
        get() = prefs.getInt(KEY_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_MINUTE, value).apply()

    /** @return false si el correo ya estaba registrado. */
    fun register(name: String, email: String, password: String): Boolean {
        val key = email.trim().lowercase()
        if (key == DEMO_EMAIL || prefs.contains("pwd_$key")) return false
        prefs.edit()
            .putString("name_$key", name.trim())
            .putString("pwd_$key", password)
            .apply()
        startSession(key, name.trim())
        return true
    }

    /** @return true si las credenciales son correctas. */
    fun login(email: String, password: String): Boolean {
        val key = email.trim().lowercase()
        if (key == DEMO_EMAIL && password == DEMO_PASSWORD) {
            startSession(key, "Demo")
            return true
        }
        val saved = prefs.getString("pwd_$key", null) ?: return false
        if (saved != password) return false
        startSession(key, prefs.getString("name_$key", "") ?: "")
        return true
    }

    fun logout() {
        prefs.edit().putBoolean(KEY_LOGGED, false).apply()
    }

    private fun startSession(email: String, name: String) {
        prefs.edit()
            .putBoolean(KEY_LOGGED, true)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, name)
            .apply()
    }

    private companion object {
        const val DEMO_EMAIL = "demo@stylesync.com"
        const val DEMO_PASSWORD = "123456"
        const val KEY_LOGGED = "logged"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_NOTIF = "notifications"
        const val KEY_HOUR = "reminder_hour"
        const val KEY_MINUTE = "reminder_minute"
    }
}