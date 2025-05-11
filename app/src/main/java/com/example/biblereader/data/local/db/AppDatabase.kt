package com.example.biblereader.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.biblereader.data.local.db.dao.*
import com.example.biblereader.data.local.db.entities.*

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        VerseEntity::class,
        HeadingEntity::class,
        LanguageEntity::class,
        TranslationEntity::class,
        BookmarkEntity::class,
        HighlightEntity::class
    ],
    version = 1, // Start with version 1. Increment if you change the schema later.
    exportSchema = false // Optional: Set to true to export schema to a folder
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods for Room to implement
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun headingDao(): HeadingDao
    abstract fun languageDao(): LanguageDao
    abstract fun translationDao(): TranslationDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // !!! IMPORTANT: Replace "bible.db" with the actual filename of your preloaded database asset !!!
        private const val DATABASE_NAME = "bible.db"
        private const val ASSET_DB_PATH = "database/$DATABASE_NAME" // Path within the assets folder

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // Wipes and rebuilds instead of migrating if no Migration object specified.
                    .fallbackToDestructiveMigration() // Added this line to handle schema mismatches
                    .createFromAsset(ASSET_DB_PATH) // Copy the preloaded database
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
