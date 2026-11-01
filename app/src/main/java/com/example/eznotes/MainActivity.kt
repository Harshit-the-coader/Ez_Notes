package com.example.eznotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.eznotes.ui.navigation.NotesApp
import com.example.eznotes.ui.theme.EzNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EzNotesTheme {
                NotesApp()
            }
        }
    }
}