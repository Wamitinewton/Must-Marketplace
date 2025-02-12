package com.newton.mustmarket.features.merchant.get_merchants.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.get_merchants.presentation.events.GetMerchantsEvents
import com.newton.mustmarket.features.merchant.get_merchants.presentation.state.GetMerchantByIdState
import com.newton.mustmarket.features.merchant.get_merchants.presentation.state.GetMerchantsState
import com.newton.mustmarket.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMerchantsViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _getAllMerchantsState = MutableStateFlow(GetMerchantsState())
    val getAllMerchantsEvent = _getAllMerchantsState.asStateFlow()

    private val _getMerchantByIdState = MutableStateFlow(GetMerchantByIdState())
    val getMerchantByIdState = _getMerchantByIdState.asStateFlow()

    private val _currentMerchantIndex = MutableStateFlow(0)
    val currentMerchantIndex = _currentMerchantIndex.asStateFlow()

    init {
        getAllMerchants()
    }

    fun updateCurrentIndex(index: Int) {
        _currentMerchantIndex.update { index }
    }


    fun handleEvent(events: GetMerchantsEvents) {
        when(events){
            GetMerchantsEvents.GetAllMerchants -> getAllMerchants()
            is GetMerchantsEvents.GetMerchantById -> getMerchantById(id = events.id)
        }
    }

    private fun getMerchantById(id: Int) {
        try {
            viewModelScope.launch {
                useCases.getMerchantsUseCase.getMerchantById(id).collect { merchandById ->
                    when (merchandById) {
                        is Resource.Error -> {
                            _getMerchantByIdState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    error = merchandById.message,
                                    success = null
                                )
                            }
                        }

                        is Resource.Loading -> {
                            _getMerchantByIdState.update { currentState ->
                                currentState.copy(
                                    isLoading = true,
                                    error = null,
                                    success = null
                                )
                            }
                        }

                        is Resource.Success -> {
                            _getMerchantByIdState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    error = null,
                                    success = merchandById.data
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _getMerchantByIdState.update { currentState ->
                currentState.copy(
                    error = e.message,
                    isLoading = false,
                    success = null
                )
            }
        }
    }

    private fun getAllMerchants() {
        try {
            viewModelScope.launch {
                useCases.getMerchantsUseCase.getAllMerchants().collect { merchants ->
                    when(merchants) {
                        is Resource.Error -> {
                            _getAllMerchantsState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    error = merchants.message,
                                    success = null
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _getAllMerchantsState.update { currentState ->
                                currentState.copy(
                                    isLoading = true,
                                    error = null,
                                    success = null
                                )
                            }
                        }
                        is Resource.Success -> {
                            _getAllMerchantsState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    error = null,
                                    success = merchants.data
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _getAllMerchantsState.update { currentState ->
                currentState.copy(
                    error = e.message,
                    isLoading = false,
                    success = null
                )
            }
        }
    }
}