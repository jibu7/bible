package com.example.biblereader.util

import androidx.test.espresso.IdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataStoreIdlingResource(
    private val scope: CoroutineScope,
    private val flow: Flow<*>
) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle = true
    private var job: Job? = null

    override fun getName(): String = "DataStoreIdlingResource"

    override fun isIdleNow(): Boolean {
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    fun startMonitoring() {
        isIdle = false
        job = scope.launch {
            flow.collect {
                isIdle = true
                resourceCallback?.onTransitionToIdle()
            }
        }
    }

    fun stopMonitoring() {
        job?.cancel()
        isIdle = true
    }
} 