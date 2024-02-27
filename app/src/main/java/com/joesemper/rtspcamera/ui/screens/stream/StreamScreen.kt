package com.joesemper.rtspcamera.ui.screens.stream

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.alexvas.rtsp.widget.RtspSurfaceView
import com.joesemper.rtspcamera.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

@Composable
fun StreamScreen(
    navigateHome: () -> Unit,
    viewModel: StreamViewModel = getViewModel()
) {

    val state = viewModel.uiState

    var status = viewModel.uiState.status.getString()

    var showLayout by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val videoViewModifier = remember(configuration.orientation) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true)
                    .border(width = 0.1.dp, color = Color.Red)
            }

            else -> {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = false)
                    .border(width = 0.1.dp, color = Color.Red)
            }
        }
    }


    val listener = object : RtspSurfaceView.RtspStatusListener {
        override fun onRtspStatusConnecting() {
            super.onRtspStatusConnecting()
            viewModel.onConnecting()
        }

        override fun onRtspStatusDisconnected() {
            super.onRtspStatusDisconnected()
            viewModel.onError(context.getString(R.string.disconnected))
        }

        override fun onRtspStatusFailedUnauthorized() {
            super.onRtspStatusFailedUnauthorized()
            viewModel.onError(context.getString(R.string.unauthorized))
        }

        override fun onRtspStatusFailed(message: String?) {
            super.onRtspStatusFailed(message)
            viewModel.onError(message ?: "")
        }

        override fun onRtspStatusConnected() {
            super.onRtspStatusConnected()
            viewModel.onConnected()
        }

        override fun onRtspFirstFrameRendered() {
            super.onRtspFirstFrameRendered()
            status = "First frame rendered"
        }

        override fun onRtspStatusDisconnecting() {
            super.onRtspStatusDisconnecting()
            status = "Disconnecting"
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (state.status != StreamStatus.Loading) {

            val uri = remember(viewModel.uiState) { Uri.parse(state.currentUri) }

            AndroidView(
                modifier = videoViewModifier,
                factory = { context ->
                    RtspSurfaceView(context).apply {
                        init(uri, null, null)
                        setStatusListener(listener)
                        start(requestVideo = true, requestAudio = false)
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    showLayout = true
                },
            contentAlignment = Alignment.TopStart
        ) {
            AnimatedVisibility(
                visible = showLayout,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                LaunchedEffect(key1 = context) {
                    delay(5000)
                    showLayout = false
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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