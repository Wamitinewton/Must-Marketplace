package com.newton.mustmarket.features.home.presentation.event

import android.content.Context
import android.net.Uri

sealed class CategoryEvent {
    data object Refresh : CategoryEvent()
    data class CategoryUploadEvent(val uri: Uri, val name: String, val context: Context) :
        CategoryEvent()

}