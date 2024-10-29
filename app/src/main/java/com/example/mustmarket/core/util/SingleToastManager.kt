package com.example.mustmarket.core.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object SingleToastManager {
    private var currentToast: Toast? = null
    private var currentJob: Job? = null

    fun showToast(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT,
        scope: CoroutineScope
    ){
        currentJob?.cancel()

        currentToast?.cancel()

        currentJob = scope.launch {
            currentToast = Toast.makeText(context, message, duration).also {
                it.show()
            }
        }
    }

    fun cancelCurrent() {
        currentJob?.cancel()
        currentToast?.cancel()
        currentToast = null
    }

}