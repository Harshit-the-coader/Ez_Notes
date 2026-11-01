package com.example.eznotes.data.repository

import com.example.eznotes.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeNotesRepository : NotesRepository {

    private val notesFlow = MutableStateFlow<List<NoteEntity>>(emptyList())
    private var nextId = 1L

    override fun getNotes(query: String, sortOption: SortOption): Flow<List<NoteEntity>> {
        return notesFlow.map { notes ->
            notes.filter {
                it.title.contains(query, ignoreCase = true) || 
                it.content.contains(query, ignoreCase = true)
            }.sortedWith(
                compareByDescending<NoteEntity> { it.isPinned }
                    .thenBy { if (sortOption == SortOption.TITLE) it.title.lowercase() else "" }
                    .thenByDescending { if (sortOption == SortOption.DATE) it.updatedAt else 0L }
            )
        }
    }

    override fun getNoteById(id: Long): Flow<NoteEntity?> {
        return notesFlow.map { notes -> notes.find { it.id == id } }
    }

    override suspend fun insertNote(note: NoteEntity): Long {
        val id = if (note.id == 0L) nextId++ else note.id
        val newNote = note.copy(id = id)
        notesFlow.update { current ->
            current.filterNot { it.id == id } + newNote
        }
        return id
    }

    override suspend fun updateNote(note: NoteEntity) {
        notesFlow.update { current ->
            current.filterNot { it.id == note.id } + note
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        deleteNoteById(note.id)
    }

    override suspend fun deleteNoteById(id: Long) {
        notesFlow.update { current ->
            current.filterNot { it.id == id }
        }
    }
    
    fun clear() {
        notesFlow.value = emptyList()
        nextId = 1L
    }
}