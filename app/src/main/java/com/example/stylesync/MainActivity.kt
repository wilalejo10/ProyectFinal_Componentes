package com.example.stylesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stylesync.navigation.StyleSyncApp
import com.example.stylesync.ui.theme.StyleSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StyleSyncTheme {
                StyleSyncApp()
            }
        }
    }
}