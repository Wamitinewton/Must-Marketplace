package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mustmarket.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val categoryUseCases: UseCases
): ViewModel() {
}