package com.example.github

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.useCases.ReadMessageTokenUseCase
import com.example.domain.useCases.SaveMessageTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val saveMessageTokenUseCase: SaveMessageTokenUseCase,
    private val readMessageTokenUseCase: ReadMessageTokenUseCase,
) : ViewModel() {
    private val mutableMessageTokenState: MutableStateFlow<String> = MutableStateFlow("")
    val messageTokenState: StateFlow<String> = mutableMessageTokenState.asStateFlow()

    init {
        saveMessageToken()
        readMessageToken()
    }

    private fun saveMessageToken() {
        viewModelScope.launch {
            saveMessageTokenUseCase()
        }
    }

    fun readMessageToken() {
        viewModelScope.launch {
            readMessageTokenUseCase.invoke().collect { token: String ->
                mutableMessageTokenState.value = token
            }
        }
    }
}
