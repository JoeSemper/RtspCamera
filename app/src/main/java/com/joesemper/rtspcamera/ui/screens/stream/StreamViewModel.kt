package com.joesemper.rtspcamera.ui.screens.stream

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.rtspcamera.R
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class StreamViewModel(
    private val datastore: RtspDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(StreamScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            datastore.getCurrentStreamUri().collect { uri ->
                _uiState.value = if (uri.isEmpty()) {
                    _uiState.value.copy(
                        statusLog = _uiState.value.statusLog.plus(StreamStatus.Error("Empty Uri"))
                    )
                } else {
                    _uiState.value.copy(
                        currentUri = uri,
                        statusLog = _uiState.value.statusLog.plus(StreamStatus.Ready)
                    )
                }
            }
        }
    }

    fun updateStatus(newStatus: StreamStatus) {
        _uiState.value = _uiState.value.copy(statusLog = _uiState.value.statusLog.plus(newStatus))
    }

}

data class StreamScreenState(
    val currentUri: String = "",
    val statusLog: List<StreamStatus> = listOf(StreamStatus.Loading),
) {
    val status: StreamStatus
        get() = statusLog.maxByOrNull { it.date }!!
}

sealed class StreamStatus {
    @Composable
    abstract fun getString(): String
    val date = Calendar.getInstance().time.time

    object Loading : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.loading)
    }

    object Ready : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.ready_to_connect)
    }

    class Error(val massage: String = "") : StreamStatus() {
        @Composable
        override fun getString(): String = massage.ifEmpty { stringResource(R.string.error) }
    }

    object Connecting : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.connecting)
    }

    object Disconnected : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(id = R.string.disconnected)
    }

    object Disconnecting : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.disconnecting)
    }

    object Connected : StreamStatus() {
        @Composable
        override fun getString(): String = stringResource(R.string.connected)

    }
}

