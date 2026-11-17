package com.example.stylesync

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stylesync.navigation.StyleSyncApp
import com.example.stylesync.ui.theme.StyleSyncTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- PRUEBA TEMPORAL DE FIREBASE (borrar después) ---
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener { task ->
                Log.d("Firebase", "Auth anónima: ${task.isSuccessful} ${task.exception?.message}")
            }
        }
        FirebaseFirestore.getInstance().collection("prueba")
            .add(mapOf("hola" to "mundo"))
            .addOnSuccessListener { Log.d("Firebase", "Firestore funciona") }
            .addOnFailureListener { Log.e("Firebase", "Firestore error", it) }
        // --- FIN DE LA PRUEBA ---

        setContent {
            StyleSyncTheme {
                StyleSyncApp()
            }
        }
    }
}