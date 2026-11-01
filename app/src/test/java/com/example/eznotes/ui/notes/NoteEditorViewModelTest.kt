package com.example.eznotes.ui.notes

import com.example.eznotes.data.repository.FakeNotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteEditorViewModelTest {

    private lateinit var viewModel: NoteEditorViewModel
    private lateinit var repository: FakeNotesRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNotesRepository()
        viewModel = NoteEditorViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveNote with blank title and content sets validation error`() = runTest {
        viewModel.loadNote(null)
        viewModel.updateTitle("   ")
        viewModel.updateContent("")
        
        viewModel.saveNote()
        
        assertEquals("Cannot save an empty note", viewModel.uiState.value.validationError)
        assertEquals(false, viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveNote with blank title but valid content saves as Untitled note`() = runTest {
        viewModel.loadNote(null)
        viewModel.updateTitle("")
        viewModel.updateContent("Some valid content")
        
        viewModel.saveNote()
        advanceUntilIdle()
        
        assertEquals("Untitled note", viewModel.uiState.value.title)
        assertEquals(true, viewModel.uiState.value.isSaved)
    }
}