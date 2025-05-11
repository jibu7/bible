package com.example.biblereader.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.biblereader.data.local.preferences.ThemePreferences
import com.example.biblereader.data.preferences.UserPreferencesRepository
import com.example.biblereader.repository.AppRepository
import com.example.biblereader.repository.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideThemePreferences(dataStore: DataStore<Preferences>): ThemePreferences {
        return ThemePreferences(dataStore)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }
}
