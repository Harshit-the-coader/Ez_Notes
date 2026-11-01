package com.example.eznotes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    
    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' COLLATE NOCASE 
        OR content LIKE '%' || :query || '%' COLLATE NOCASE 
        ORDER BY 
        isPinned DESC, 
        CASE WHEN :sortBy = 'TITLE' THEN title END COLLATE NOCASE ASC,
        CASE WHEN :sortBy = 'DATE' THEN updatedAt END DESC
    """)
    fun getNotes(query: String, sortBy: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}