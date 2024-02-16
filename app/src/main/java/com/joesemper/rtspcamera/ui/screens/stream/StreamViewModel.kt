package com.joesemper.rtspcamera.ui.screens.stream

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StreamViewModel : ViewModel() {
    var uiState by mutableStateOf("Stream")
        private set
}