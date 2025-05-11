# Bible App Product Requirements Document (PRD) and Development Tree

## Product Requirements Document (PRD)

### 1. Functional Requirements

- **Bible Navigation**: Users can view the Bible organized hierarchically by Testament → Book → Chapter → Verse.
- **Section Headings**: Display headings for sections within chapters to group verses.
- **Multi-language Support**: Allow users to select a preferred language for viewing the Bible.
- **Verse Translations**: Display verse content in the translation mapped to the selected language.
- **Search Functionality**: Enable searching by verse reference, keyword within verses, or book name.
- **Offline Usage**: Ensure all Bible data and features are available offline, stored locally.
- **Bookmark and Highlight**: Provide options for users to bookmark or highlight verses for later reference.
- **Dark/Light Mode** (Optional): Support switching between dark and light themes as a desirable feature.

### 2. Non-Functional Requirements

- **Performance**: Achieve fast local queries using Room for a responsive experience.
- **UI/UX**: Design an intuitive and easy-to-navigate user interface.
- **Architecture**: Implement a modular and testable architecture using MVVM principles.
- **Backup**: Support exporting and importing bookmarks and highlights for data portability.

### 3. Tech Stack

- **Language**: Kotlin
- **Database**: Room + SQLite (preloaded from an asset)
- **UI**: Jetpack Compose (preferred) or XML Views
- **Architecture**: MVVM
- **Dependency Injection**: Hilt or Koin
- **Navigation**: Jetpack Navigation Component

### 4. Database Model

- **Entities**:
  - `BookEntity`: Represents a book in the Bible (e.g., Genesis).
  - `ChapterEntity`: Represents a chapter within a book.
  - `VerseEntity`: Represents individual verses.
  - `HeadingEntity`: Represents section headings within chapters.
  - `LanguageEntity`: Represents available languages.
  - `TranslationEntity`: Maps verses to their translations in different languages.
  - Additional entities: `BookmarkEntity` and `HighlightEntity` for user data.
- **Note**: The `bible_staging` table is used offline to populate the database and is not included in the final app.

---

## Development Tree

The development process is structured into phases for a systematic and modular approach.

### 1. Project Setup

- Create a new Android project with Kotlin.
- Add dependencies: Room, Hilt/Koin, Jetpack Navigation, and Jetpack Compose (or XML Views).

### 2. Database Preparation

- Populate the SQLite database with Bible data (books, chapters, verses, headings, languages, translations) using the `bible_staging` table or another source.
- Embed the preloaded database as an app asset.
- Optimize with indexes (e.g., on verse text, book names) for efficient queries.

### 3. Define Room Entities and DAOs

- Implement Room entities: `BookEntity`, `ChapterEntity`, `VerseEntity`, `HeadingEntity`, `LanguageEntity`, `TranslationEntity`, `BookmarkEntity`, `HighlightEntity`.
- Define relationships with foreign keys (e.g., verses linked to chapters).
- Create DAOs for CRUD operations on each entity, including search-specific queries.

### 4. Implement Repositories

- Develop repository classes to interface with DAOs.
- Provide methods for fetching Bible data (e.g., books, chapters, verses) and user data (bookmarks, highlights).
- Implement search logic for verse, keyword, and book searches.

### 5. Set Up Navigation

- Configure Jetpack Navigation Component with a navigation graph.
- Define screens: Testament list, Book list, Chapter list, Verse view, Search, Bookmarks/Highlights, Settings.

### 6. Implement Bible Navigation UI

- Create UI components (using Jetpack Compose or XML) to display:
  - Lists of Testaments, Books, Chapters, and Verses.
  - Verse content with proper formatting.
- Enable navigation between hierarchy levels.

### 7. Add Section Headings

- Enhance the chapter view to include section headings.
- Associate headings with the correct verse ranges.

### 8. Implement Language Selection

- Add a language selector in the settings or UI.
- Update verse display to reflect the selected language’s translation.

### 9. Implement Search Functionality

- Design a search screen with input for queries.
- Implement repository methods to support:
  - Verse reference search (e.g., "John 3:16").
  - Keyword search within verse text.
  - Book name search.
- Display results with navigation to the matching verse.

### 10. Add Bookmark and Highlight Features

- Add UI controls (e.g., buttons or icons) to bookmark or highlight verses.
- Store bookmarks and highlights in Room using `BookmarkEntity` and `HighlightEntity`.
- Create a screen to view and manage saved bookmarks and highlights.

### 11. Implement Export/Import for User Data

- Enable exporting bookmarks and highlights to a file (e.g., JSON format).
- Enable importing from a file to restore user data.
- Ensure compatibility for cross-device transfers.

### 12. Add Dark/Light Mode Support (Optional)

- Implement theme switching in the settings.
- Apply dark and light themes across the app UI.

### 13. Testing

- Write unit tests for repositories and ViewModels.
- Write instrumented tests for UI and key workflows.
- Verify offline functionality and query performance.

### 14. Polish and Bug Fixing

- Refine UI/UX for a seamless experience.
- Address bugs and ensure compliance with all requirements.

---

## Summary

The Bible app delivers an offline-capable, multi-language Bible reader with search, bookmarking, and highlighting features. Built with a modular MVVM architecture, it ensures maintainability and testability. The development tree provides a clear roadmap, starting with database setup, followed by data access and UI implementation, and concluding with feature enhancements and testing.

---

## Suggested Git Commit for Recent Changes

The following is a suggested git commit message that you can use for the recent updates made to the Development Tree (likely sections 6-10, based on your selection):

```text
docs: Detail core feature implementation in Development Tree

This commit elaborates on several key sections of the Development Tree,
providing detailed implementation guidance for:
- Bible Navigation UI (Section 6)
- Section Headings (Section 7)
- Language Selection (Section 8)
- Search Functionality (Section 9)
- Bookmark and Highlight Features (Section 10)

These enhancements to the PRD clarify the development path for
essential user-facing functionalities of the Bible app.