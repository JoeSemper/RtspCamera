package com.joesemper.rtspcamera.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val datastore: RtspDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            datastore.getCurrentSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    settings = settings
                )
            }
        }
    }

    fun updateStreamUri(uri: String) {
        viewModelScope.launch {
            datastore.setCurrentStreamUri(uri)
        }
    }

    fun updateEnableVideo(enable: Boolean) {
        viewModelScope.launch {
            datastore.setEnableVideo(enable)
        }
    }

    fun updateEnableAudio(enable: Boolean) {
        viewModelScope.launch {
            datastore.setEnableAudio(enable)
        }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            datastore.setUsername(username)
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            datastore.setPassword(password)
        }
    }

    fun updateRatio(ratio: Float) {
        viewModelScope.launch {
            datastore.setAspectRatio(ratio)
        }
    }

    fun updateEnableReconnectTimeout(enable: Boolean) {
        viewModelScope.launch {
            datastore.setEnableReconnectTimeout(enable)
        }
    }
}

data class SettingsUiState(
    val isLoading: Boolean = true,
    val settings: StreamSettings = StreamSettings()
)

data class StreamSettings(
    val streamLink: String = "",
    val enableVideo: Boolean = true,
    val enableAudio: Boolean = true,
    val username: String = "",
    val password: String = "",
    val ratio: Float = 1f,
    val enableReconnectTimeout: Boolean = false
)

enum class AspectRatio(val w: Int, val h: Int) {
    ONE_TO_ONE(1, 1),
    FOUR_TO_THREE(4, 3),
    FIVE_TO_FOUR(5, 4),
    THREE_TO_TWO(3, 2),
    SIXTEEN_TO_NINE(16, 9);

    val ratio: Float = w.toFloat() / h.toFloat()
}