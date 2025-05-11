package com.example.biblereader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Empty content - will be set by tests
        }
    }
}

@Composable
fun TestContent(content: @Composable () -> Unit) {
    content()
} 