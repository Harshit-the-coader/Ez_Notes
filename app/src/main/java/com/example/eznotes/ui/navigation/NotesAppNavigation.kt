package com.example.eznotes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eznotes.ui.notes.NoteEditorScreen
import com.example.eznotes.ui.notes.NotesListScreen

sealed class Screen(val route: String) {
    object NotesList : Screen("notes_list")
    object NoteEditor : Screen("note_editor?noteId={noteId}") {
        fun createRoute(noteId: Long? = null): String {
            return if (noteId != null) "note_editor?noteId=$noteId" else "note_editor"
        }
    }
}

@Composable
fun NotesApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.NotesList.route
    ) {
        composable(Screen.NotesList.route) {
            NotesListScreen(
                onNavigateToEditor = { noteId ->
                    navController.navigate(Screen.NoteEditor.createRoute(noteId))
                }
            )
        }
        
        composable(
            route = Screen.NoteEditor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val noteIdStr = backStackEntry.arguments?.getString("noteId")
            val noteId = noteIdStr?.toLongOrNull()
            NoteEditorScreen(
                noteId = noteId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}