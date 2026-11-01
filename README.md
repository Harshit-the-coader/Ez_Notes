# Ez Notes - Offline-First Notes App

## Project Overview

Ez Notes is an Android notes application that works **fully offline**. Users can create, view, edit, delete, search, sort and organize personal notes. All notes are stored on the device using a Room database, so they remain available after the app is closed or the device is restarted. The app needs no backend, account, API key or internet connection.

**Key features**
- Create, edit and delete notes (with delete confirmation)
- Search by title and body text (case-insensitive) with a no-results state
- Sort by last updated (newest first) or title (A-Z)
- Empty state on the home screen
- Blank notes cannot be saved; a blank title with body text is saved as "Untitled note"
- All core features work in airplane mode

## Setup and Build Instructions

**Requirements:** Android Studio (latest stable), JDK 17, and an emulator or device running Android 7.0 (API 24) or higher.

1. Clone the repository:
   ```bash
   git clone https://github.com/Harshit-the-coader/Ez_Notes.git
   ```
2. Open the project in Android Studio and wait for the Gradle sync to finish.
3. Select an emulator or a connected device and click **Run**.

**Build the APK:**
- Android Studio: **Build > Build Bundle(s) / APK(s) > Build APK(s)**
- Or via terminal:
  ```bash
  ./gradlew assembleDebug
  ```
- Output: `app/build/outputs/apk/debug/app-debug.apk`

## Architecture Summary

The app follows **MVVM** with a clear separation between the UI, ViewModel and data layers.

```
Compose UI  <--  ViewModel (StateFlow)  <--  Repository  <--  DAO  <--  Room
```

- **data/local:** Room entity, DAO and database
- **data/repository:** `NotesRepository`, the only data access point used by the ViewModels
- **ui/notes:** list screen, editor screen, ViewModels and UI state
- **ui/navigation:** navigation routes

Room exposes notes as a `Flow`. The ViewModel combines that flow with the current search query and sort option, exposes the result as a `StateFlow`, and Compose collects it with `collectAsStateWithLifecycle()`, so the UI updates automatically whenever the database changes.

## Libraries Used

- Kotlin
- Jetpack Compose (Material 3)
- Room (with KSP)
- Kotlin Coroutines and Flow / StateFlow
- Navigation Compose
- AndroidX Lifecycle (ViewModel)

## Known Limitations

- No note categories or colors
- No UI (instrumented) tests
- No backup, export or import of notes
- Tablet and landscape layouts are functional but not specially optimized

## Author

**Harshit Singh**
Email: hs26112005@gmail.com
GitHub: [Harshit-the-coader](https://github.com/Harshit-the-coader)
# Ez Notes - Offline-First Notes App

## Project Overview

Ez Notes is an Android notes application that works **fully offline**. Users can create, view, edit, delete, search, sort and organize personal notes. All notes are stored on the device using a Room database, so they remain available after the app is closed or the device is restarted. The app needs no backend, account, API key or internet connection.

**Key features**
- Create, edit and delete notes (with delete confirmation)
- Search by title and body text (case-insensitive) with a no-results state
- Sort by last updated (newest first) or title (A-Z)
- Empty state on the home screen
- Blank notes cannot be saved; a blank title with body text is saved as "Untitled note"
- All core features work in airplane mode

## Setup and Build Instructions

**Requirements:** Android Studio (latest stable), JDK 17, and an emulator or device running Android 7.0 (API 24) or higher.

1. Clone the repository:
   ```bash
   git clone https://github.com/Harshit-the-coader/Ez_Notes.git
   ```
2. Open the project in Android Studio and wait for the Gradle sync to finish.
3. Select an emulator or a connected device and click **Run**.

**Build the APK:**
- Android Studio: **Build > Build Bundle(s) / APK(s) > Build APK(s)**
- Or via terminal:
  ```bash
  ./gradlew assembleDebug
  ```
- Output: `app/build/outputs/apk/debug/app-debug.apk`

## Architecture Summary

The app follows **MVVM** with a clear separation between the UI, ViewModel and data layers.

```
Compose UI  <--  ViewModel (StateFlow)  <--  Repository  <--  DAO  <--  Room
```

- **data/local:** Room entity, DAO and database
- **data/repository:** `NotesRepository`, the only data access point used by the ViewModels
- **ui/notes:** list screen, editor screen, ViewModels and UI state
- **ui/navigation:** navigation routes

Room exposes notes as a `Flow`. The ViewModel combines that flow with the current search query and sort option, exposes the result as a `StateFlow`, and Compose collects it with `collectAsStateWithLifecycle()`, so the UI updates automatically whenever the database changes.

## Libraries Used

- Kotlin
- Jetpack Compose (Material 3)
- Room (with KSP)
- Kotlin Coroutines and Flow / StateFlow
- Navigation Compose
- AndroidX Lifecycle (ViewModel)

## Known Limitations

- No note categories or colors
- No UI (instrumented) tests
- No backup, export or import of notes
- Tablet and landscape layouts are functional but not specially optimized

## Author

**Harshit Singh**
Email: hs26112005@gmail.com
GitHub: [Harshit-the-coader](https://github.com/Harshit-the-coader)
