package com.example.eznotes.di

import android.content.Context
import com.example.eznotes.data.local.NotesDatabase
import com.example.eznotes.data.repository.NotesRepository
import com.example.eznotes.data.repository.OfflineNotesRepository

interface AppContainer {
    val notesRepository: NotesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    
    private val database by lazy { NotesDatabase.getDatabase(context) }
    
    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(database.noteDao())
    }
}