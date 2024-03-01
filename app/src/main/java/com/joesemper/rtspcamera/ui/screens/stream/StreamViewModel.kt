package com.joesemper.rtspcamera.ui.screens.stream

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.rtspcamera.R
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import com.joesemper.rtspcamera.ui.screens.home.StreamSettings
import com.joesemper.rtspcamera.utils.unixTimeToHoursMinutesSeconds
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
            datastore.getCurrentSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    statusLog = if (settings.streamLink.isEmpty()) {
                        _uiState.value.statusLog.plus(StreamStatus.Error("Empty Uri"))
                    } else {
                        _uiState.value.statusLog.plus(StreamStatus.Ready())
                    }
                )
            }
        }
    }

    fun updateStatus(newStatus: StreamStatus) {
        _uiState.value = _uiState.value.copy(
            statusLog = _uiState.value.statusLog.plus(newStatus).sortedBy { it.date })
    }

    fun incrementReconnectCount() {
        _uiState.value = _uiState.value.copy(
            reconnectCount = _uiState.value.reconnectCount + 1
        )
    }

}

data class StreamScreenState(
    val settings: StreamSettings = StreamSettings(),
    val statusLog: List<StreamStatus> = listOf(StreamStatus.Loading()),
    val reconnectCount: Int = 0
) {
    val status: StreamStatus
        get() = statusLog.maxByOrNull { it.date }!!
}

sealed class StreamStatus {
    @Composable
    abstract fun getText(): String
    val date = Calendar.getInstance().time.time
    val timeText = unixTimeToHoursMinutesSeconds(date)

    class Loading : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.loading)
    }

    class Ready : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.ready_to_connect)
    }

    class Error(val massage: String = "") : StreamStatus() {
        @Composable
        override fun getText(): String = massage.ifEmpty { stringResource(R.string.error) }
    }

    class Connecting : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.connecting)
    }

    class Disconnected : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(id = R.string.disconnected)
    }

    class Disconnecting : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.disconnecting)
    }

    class Connected : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.connected)

    }

    class Reconnecting(val count: Int) : StreamStatus() {
        @Composable
        override fun getText(): String = stringResource(R.string.reconnecting) + ":" + count
    }
}

