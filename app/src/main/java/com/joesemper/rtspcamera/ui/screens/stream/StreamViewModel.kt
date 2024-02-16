package com.joesemper.rtspcamera.ui.screens.stream

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import kotlinx.coroutines.launch

class StreamViewModel(
    private val datastore: RtspDataStore
) : ViewModel() {
    var uiState by mutableStateOf("")
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            datastore.getCurrentStreamUri().collect{ uri ->
                uiState = uri
            }
        }
    }
}