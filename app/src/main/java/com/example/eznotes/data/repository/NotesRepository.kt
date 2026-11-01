package com.example.eznotes.data.repository

import com.example.eznotes.data.local.NoteDao
import com.example.eznotes.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotes(query: String, sortOption: SortOption): Flow<List<NoteEntity>>
    fun getNoteById(id: Long): Flow<NoteEntity?>
    suspend fun insertNote(note: NoteEntity): Long
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    suspend fun deleteNoteById(id: Long)
}

class OfflineNotesRepository(private val noteDao: NoteDao) : NotesRepository {
    
    override fun getNotes(query: String, sortOption: SortOption): Flow<List<NoteEntity>> {
        // Escape wildcards in SQLite LIKE queries
        val escapedQuery = query
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
            .trim()
            
        return noteDao.getNotes(escapedQuery, sortOption.name)
    }

    override fun getNoteById(id: Long): Flow<NoteEntity?> {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }
}