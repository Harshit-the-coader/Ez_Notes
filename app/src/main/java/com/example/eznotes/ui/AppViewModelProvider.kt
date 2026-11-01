package com.example.eznotes.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.eznotes.EzNotesApplication
import com.example.eznotes.ui.notes.NoteEditorViewModel
import com.example.eznotes.ui.notes.NotesListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NotesListViewModel(
                ezNotesApplication().container.notesRepository
            )
        }
        initializer {
            NoteEditorViewModel(
                ezNotesApplication().container.notesRepository
            )
        }
    }
}

fun CreationExtras.ezNotesApplication(): EzNotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EzNotesApplication)