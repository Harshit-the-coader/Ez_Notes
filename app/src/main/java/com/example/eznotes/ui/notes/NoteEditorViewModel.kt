package com.example.eznotes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eznotes.data.local.NoteEntity
import com.example.eznotes.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditorUiState(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val validationError: String? = null
)

class NoteEditorViewModel(
    private val repository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var initialNoteState: NoteEditorUiState? = null

    fun loadNote(id: Long?) {
        if (id == null) {
            initialNoteState = _uiState.value
            return
        }
        
        viewModelScope.launch {
            repository.getNoteById(id).collect { note ->
                if (note != null) {
                    val loadedState = NoteEditorUiState(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                        isPinned = note.isPinned,
                        createdAt = note.createdAt
                    )
                    _uiState.value = loadedState
                    initialNoteState = loadedState
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title, validationError = null) }
    }

    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content, validationError = null) }
    }
    
    fun togglePin() {
        _uiState.update { it.copy(isPinned = !it.isPinned) }
    }

    fun hasUnsavedChanges(): Boolean {
        val current = _uiState.value
        val initial = initialNoteState ?: NoteEditorUiState()
        return current.title != initial.title || current.content != initial.content || current.isPinned != initial.isPinned
    }

    fun saveNote() {
        val currentState = _uiState.value
        if (currentState.isSaving) return

        val titleBlank = currentState.title.isBlank()
        val contentBlank = currentState.content.isBlank()

        if (titleBlank && contentBlank) {
            _uiState.update { it.copy(validationError = "Cannot save an empty note") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        val finalTitle = if (titleBlank) "Untitled note" else currentState.title.trim()
        val currentTime = System.currentTimeMillis()

        val note = NoteEntity(
            id = currentState.id ?: 0L,
            title = finalTitle,
            content = currentState.content.trim(),
            createdAt = if (currentState.id == null || currentState.createdAt == 0L) currentTime else currentState.createdAt,
            updatedAt = currentTime,
            isPinned = currentState.isPinned
        )

        viewModelScope.launch {
            if (note.id == 0L) {
                val newId = repository.insertNote(note)
                _uiState.update { it.copy(id = newId, title = finalTitle, isSaving = false, isSaved = true) }
            } else {
                repository.updateNote(note)
                _uiState.update { it.copy(title = finalTitle, isSaving = false, isSaved = true) }
            }
        }
    }

    fun deleteNote() {
        val id = _uiState.value.id ?: return
        viewModelScope.launch {
            repository.deleteNoteById(id)
            _uiState.update { it.copy(isSaved = true) } // trigger navigation back
        }
    }
    
    fun dismissValidationError() {
        _uiState.update { it.copy(validationError = null) }
    }
}