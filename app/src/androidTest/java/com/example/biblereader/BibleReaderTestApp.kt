package com.example.biblereader

import dagger.hilt.android.testing.CustomTestApplication

/**
 * Custom test application interface for Hilt tests.
 * This will generate a test application class that extends BaseApplication.
 */
@CustomTestApplication(BaseApplication::class)
interface BibleReaderTestApp 