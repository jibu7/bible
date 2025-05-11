package com.example.biblereader.di

import androidx.room.Room
import android.content.Context
import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.data.local.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "bible.db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset("database/$DATABASE_NAME")
            .fallbackToDestructiveMigration()
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