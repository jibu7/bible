package com.example.biblereader

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Custom test runner that enables Hilt for Android tests.
 * This is required for Hilt dependency injection to work in instrumented tests.
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, BibleReaderTestApp_Application::class.java.name, context)
    }
} 