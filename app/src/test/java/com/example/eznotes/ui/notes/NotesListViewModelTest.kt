package com.example.eznotes.ui.notes

import com.example.eznotes.data.local.NoteEntity
import com.example.eznotes.data.repository.FakeNotesRepository
import com.example.eznotes.data.repository.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesListViewModelTest {

    private lateinit var viewModel: NotesListViewModel
    private lateinit var repository: FakeNotesRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNotesRepository()
        viewModel = NotesListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search and sorting logic works`() = runTest(UnconfinedTestDispatcher()) {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val note1 = NoteEntity(id = 1, title = "Apple", content = "Fruit", createdAt = 1000, updatedAt = 1000)
        val note2 = NoteEntity(id = 2, title = "Banana", content = "Yellow", createdAt = 2000, updatedAt = 2000)
        val note3 = NoteEntity(id = 3, title = "Carrot", content = "Vegetable", createdAt = 500, updatedAt = 3000)
        
        repository.insertNote(note1)
        repository.insertNote(note2)
        repository.insertNote(note3)
        
        // Initial state (Sorted by DATE desc)
        viewModel.onSortOptionChanged(SortOption.DATE)
        advanceUntilIdle()
        var currentNotes = viewModel.uiState.value.notes
        assertEquals(3, currentNotes.size)
        assertEquals(3L, currentNotes[0].id) // Carrot (3000)
        assertEquals(2L, currentNotes[1].id) // Banana (2000)
        assertEquals(1L, currentNotes[2].id) // Apple (1000)

        // Sort by TITLE A-Z
        viewModel.onSortOptionChanged(SortOption.TITLE)
        advanceUntilIdle()
        currentNotes = viewModel.uiState.value.notes
        assertEquals(1L, currentNotes[0].id) // Apple
        assertEquals(2L, currentNotes[1].id) // Banana
        assertEquals(3L, currentNotes[2].id) // Carrot

        // Search "yellow"
        viewModel.onSearchQueryChanged("yellow")
        advanceUntilIdle()
        currentNotes = viewModel.uiState.value.notes
        assertEquals(1, currentNotes.size)
        assertEquals(2L, currentNotes[0].id) // Banana contains "Yellow"
        
        collectJob.cancel()
    }
}