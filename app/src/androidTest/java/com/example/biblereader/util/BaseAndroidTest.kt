package com.example.biblereader.util

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import org.junit.After
import org.junit.Before

abstract class BaseAndroidTest {
    protected val idlingResources = mutableListOf<IdlingResource>()

    @Before
    open fun setup() {
        // Register any idling resources
        idlingResources.forEach { IdlingRegistry.getInstance().register(it) }
    }

    @After
    open fun tearDown() {
        // Unregister all idling resources
        idlingResources.forEach { IdlingRegistry.getInstance().unregister(it) }
        idlingResources.clear()
    }

    protected fun registerIdlingResource(idlingResource: IdlingResource) {
        idlingResources.add(idlingResource)
    }
} 