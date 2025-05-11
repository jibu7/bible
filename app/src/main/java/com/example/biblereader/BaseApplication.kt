package com.example.biblereader

import android.app.Application

/**
 * Base application class that serves as the foundation for both production and test applications.
 * This class does not include Hilt annotations, making it suitable for use with @CustomTestApplication.
 */
open class BaseApplication : Application() {
    // Add any common application logic here if needed
} 