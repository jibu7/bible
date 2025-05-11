package com.example.biblereader.di

import android.content.Context
import androidx.room.Room
import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.data.local.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideTestDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries() // Allow main thread queries for testing
            .build()
    }

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao = database.bookDao()

    @Provides
    fun provideChapterDao(database: AppDatabase): ChapterDao = database.chapterDao()

    @Provides
    fun provideVerseDao(database: AppDatabase): VerseDao = database.verseDao()

    @Provides
    fun provideHeadingDao(database: AppDatabase): HeadingDao = database.headingDao()

    @Provides
    fun provideLanguageDao(database: AppDatabase): LanguageDao = database.languageDao()

    @Provides
    fun provideTranslationDao(database: AppDatabase): TranslationDao = database.translationDao()

    @Provides
    fun provideBookmarkDao(database: AppDatabase): BookmarkDao = database.bookmarkDao()

    @Provides
    fun provideHighlightDao(database: AppDatabase): HighlightDao = database.highlightDao()
} 