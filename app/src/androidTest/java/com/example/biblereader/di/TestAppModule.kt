package com.example.biblereader.di

import android.content.Context
import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.util.TestDataSetup
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
    
    @Provides
    @Singleton
    fun provideTestDataSetup(database: AppDatabase): TestDataSetup {
        return TestDataSetup(database)
    }
} 