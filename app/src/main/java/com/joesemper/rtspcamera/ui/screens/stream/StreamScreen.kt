package com.joesemper.rtspcamera.ui.screens.stream

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.alexvas.rtsp.widget.RtspSurfaceView
import org.koin.androidx.compose.getViewModel

@Composable
fun StreamScreen(
    navigateHome: () -> Unit,
    viewModel: StreamViewModel = getViewModel()
) {

    val uri = remember(viewModel.uiState) { Uri.parse(viewModel.uiState) }

    var status by remember { mutableStateOf("") }

    val listener = object : RtspSurfaceView.RtspStatusListener {
        override fun onRtspStatusConnecting() {
            super.onRtspStatusConnecting()
            status = "Connecting"
        }

        override fun onRtspStatusDisconnected() {
            super.onRtspStatusDisconnected()
            status = "Disconnected"
        }

        override fun onRtspStatusFailedUnauthorized() {
            super.onRtspStatusFailedUnauthorized()
            status = "Unauthorized"
        }

        override fun onRtspStatusFailed(message: String?) {
            super.onRtspStatusFailed(message)
            status = message ?: "Failed"
        }

        override fun onRtspStatusConnected() {
            super.onRtspStatusConnected()
            status = "Connected"
        }

        override fun onRtspStatusDisconnecting() {
            super.onRtspStatusDisconnecting()
            status = "Disconnecting"
        }
    }

    if (viewModel.uiState.isNotEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                factory = { context ->
                    RtspSurfaceView(context).apply {
                        init(uri, null, null)
                        setStatusListener(listener)
                        start(requestVideo = true, requestAudio = false)
                    }
                }
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.alpha(0.4f),
                        text = status,
                        color = Color.White
                    )

                    IconButton(
                        modifier = Modifier.alpha(0.4f),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White),
                        onClick = { navigateHome() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                }
            }

        }
    }

}