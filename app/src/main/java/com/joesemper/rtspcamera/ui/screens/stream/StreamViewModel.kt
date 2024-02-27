package com.joesemper.rtspcamera.ui.screens.stream

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.rtspcamera.R
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import kotlinx.coroutines.launch

class StreamViewModel(
    private val datastore: RtspDataStore
) : ViewModel() {
    var uiState by mutableStateOf(StreamScreenState())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            datastore.getCurrentStreamUri().collect { uri ->
                uiState = if (uri.isEmpty()) {
                    uiState.copy(status = StreamStatus.Error("Empty Uri"))
                } else {
                    uiState.copy(currentUri = uri, status = StreamStatus.Connecting)
                }
            }
        }
    }

    fun onConnecting() {
        uiState = uiState.copy(status = StreamStatus.Connecting)
    }

    fun onConnected() {
        uiState = uiState.copy(status = StreamStatus.Connected)
    }

    fun onError(massage: String) {
        uiState = uiState.copy(status = StreamStatus.Error(massage))
    }

}

data class StreamScreenState(
    val currentUri: String = "",
    val status: StreamStatus = StreamStatus.Loading
)

sealed class StreamStatus {
    @Composable
    abstract fun getString(): String

    object Loading : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.loading)
    }

    class Error(val massage: String = "") : StreamStatus() {
        @Composable
        override fun getString(): String = massage.ifEmpty { stringResource(R.string.error) }
    }

    object Connecting : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.connecting)
    }

    object Connected : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.connected)

    }
}

