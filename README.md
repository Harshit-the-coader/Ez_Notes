# Ez Notes

Ez Notes is a complete offline-first notes application built with modern Android development practices. It requires no backend, login, API keys, or internet permissions.

## Features
- **Offline First**: All data is persisted locally using Room database.
- **Create & Edit Notes**: Add a title and content to your notes.
- **Search & Sort**: Filter notes by title and body, sort by "Last updated" or alphabetically (A-Z).
- **Pin Notes**: Pin important notes to the top of your list.
- **Swipe to Delete**: Delete notes from the home screen by swiping. Includes Undo support.
- **Data Protection**: Prevent saving blank notes, and auto-title notes with content but no title. Warns user if they try to discard unsaved edits.
- **Modern UI**: Jetpack Compose and Material 3 design system, supporting both light and dark themes. 

## Project Setup & Build Instructions
1. Clone this repository and open the project in Android Studio (Ladybug or newer).
2. The project uses standard Gradle properties. Build and run directly on an emulator or physical device running API 25+.
3. Run unit tests under the `app/src/test/` directory.

## Architecture
This app strictly follows the official Android Architecture Guidelines (UI Layer -> UI State -> ViewModel -> Repository -> Data Source).
- **Data Layer**: Room handles SQLite. `NoteEntity` defines the schema, and `NoteDao` provides data as Kotlin `Flow`s.
- **Repository**: `NotesRepository` provides a clean API for the ViewModel and abstracts the Room data source.
- **UI Layer**: Jetpack Compose for declarative UI. `NotesListViewModel` and `NoteEditorViewModel` expose unidirectional data flows (`StateFlow<UiState>`).
- **Dependency Injection**: A simple Manual Dependency Injection container (`AppContainer`) is used in the `EzNotesApplication` class to instantiate the repository exactly once for the app's lifecycle, avoiding the overhead of heavy frameworks like Hilt.

## Libraries Used
- **Jetpack Compose**: UI Toolkit (Material 3).
- **Navigation Compose**: Type-safe declarative navigation.
- **Room (KSP)**: Local SQLite persistence.
- **Lifecycle (ViewModel Compose, collectAsStateWithLifecycle)**: State and lifecycle awareness.
- **Kotlinx Coroutines & Flow**: Reactive asynchronous programming.
- **Kotlinx Coroutines Test**: For testing ViewModel logic and Flows.

## Key Design Decisions
1. **Manual DI over Hilt**: Manual DI via `AppContainer` keeps the project compilation fast and avoids the boilerplate required for simple apps, while still keeping components testable through constructor injection.
2. **StateFlow combination**: In the `NotesListViewModel`, the `searchQuery` and `sortOption` StateFlows are combined with `flatMapLatest` into a single Flow that hits the Room DAO. This ensures the UI is always perfectly in sync with the search/sort criteria, entirely reactively. Room correctly escapes SQL wildcard strings (`%`, `_`) directly in the Repository before passing it to the database query.
3. **Draft Safety**: The Editor ViewModel retains the initial state of a note when loaded. It checks `hasUnsavedChanges()` by comparing the `uiState` with the `initialNoteState`. If changes exist, a "Discard changes?" alert is presented when hitting back.
4. **Duplicate Save Prevention**: The `saveNote()` method uses a boolean lock `isSaving` in the `uiState`. As it launches a coroutine, subsequent clicks are ignored until the operation yields.
5. **Blank Note Validation**: If a user attempts to save a note with a blank title and blank body, a Snackbar error triggers rather than filling the DB with ghost rows. If only the body is present, the note assumes "Untitled note" implicitly.

## Known Limitations
- Rich text formatting (bold, italics, images) is not currently supported.
- Does not currently support labels/tags or folders for organizing notes.
- Pinned notes aren't sorted among themselves other than by the active sort selection.