package com.example.biblereader.di

import com.example.biblereader.repository.AppRepository
import com.example.biblereader.repository.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
} 