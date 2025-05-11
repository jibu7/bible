# Bible Reader Android App

## Overview

The Bible Reader is an Android application designed to provide users with an offline-capable, multi-language Bible reading experience. It features robust navigation, search capabilities, and personalization options like bookmarking and highlighting. The app is built using modern Android development practices, emphasizing a modular and testable architecture.

## Key Features

-   **Hierarchical Bible Navigation**: Easily navigate through Testaments, Books, Chapters, and Verses.
-   **Section Headings**: View section headings within chapters for better context and readability.
-   **Multi-language Support**: Select a preferred language for viewing the Bible.
-   **Verse Translations**: Access verse content in translations corresponding to the selected language.
-   **Comprehensive Search**:
    -   Search by verse reference (e.g., "John 3:16").
    -   Keyword search within verse text.
    -   Search by book name.
-   **Offline Access**: All Bible data and app features are available offline, with data stored locally.
-   **Bookmarking & Highlighting**: Mark favorite verses or highlight passages for later reference.
-   **Data Portability**: Export and import bookmarks and highlights (e.g., in JSON format).
-   **Dark/Light Mode**: Optional support for switching between dark and light themes.

## Tech Stack

-   **Language**: Kotlin
-   **User Interface**: Jetpack Compose
-   **Architecture**: Model-View-ViewModel (MVVM)
-   **Database**: Room Persistence Library with a preloaded SQLite database (from app assets).
-   **Dependency Injection**: Hilt
-   **Navigation**: Jetpack Navigation Component
-   **Asynchronous Operations**: Kotlin Coroutines

## Database

The application utilizes a preloaded SQLite database (`bible.db`) stored in the `assets/database/` directory. This database contains all necessary Bible data, including:

-   **Books** (`BookEntity`)
-   **Chapters** (`ChapterEntity`)
-   **Verses** (`VerseEntity`)
-   **Section Headings** (`HeadingEntity`)
-   **Languages** (`LanguageEntity`)
-   **Translations** (`TranslationEntity`)

User-specific data is stored in:
-   **Bookmarks** (`BookmarkEntity`)
-   **Highlights** (`HighlightEntity`)

### Database Management (Development)

A Python script (`app/src/main/assets/database/create_new_db.py`) is available for developers to manage and migrate the SQLite database schema and data. This script uses `app/src/main/assets/database/create_bible_db.sql` to define the schema.

## Project Structure

-   `app/src/main/java/com/example/biblereader/`: Main application source code.
    -   `data/`: Data layer, including local database (Room entities, DAOs, AppDatabase) and repositories.
    -   `di/`: Dependency injection modules (Hilt).
    -   `navigation/`: Navigation graph and screen definitions.
    -   `ui/`: UI-related components, including screens (Jetpack Compose), themes, and view models.
    -   `MainActivity.kt`: Main entry point of the application.
    -   `BibleReaderApplication.kt`: Custom Application class with Hilt setup.
-   `app/src/main/res/`: Android resources (drawables, layouts, values).
-   `app/src/main/assets/database/`: Contains the preloaded `bible.db` and SQL/Python scripts for database management.

## Getting Started

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Ensure the `bible.db` file is present in `app/src/main/assets/database/`.
4.  Build and run the application on an Android emulator or device.

## Future Enhancements (from PRD)

-   Refine UI/UX for a seamless experience.
-   Comprehensive unit and instrumented testing.

---

This README provides a general overview. For detailed requirements and the development roadmap, please refer to the [Bible App PRD and Development Tree.markdown](Bible%20App%20PRD%20and%20Development%20Tree.markdown) document.
