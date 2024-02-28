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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexvas.rtsp.widget.RtspSurfaceView
import com.joesemper.rtspcamera.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamScreen(
    navigateHome: () -> Unit,
    viewModel: StreamViewModel = getViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val status = state.status.getString()

    var showLayout by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val listener = object : RtspSurfaceView.RtspStatusListener {
        override fun onRtspStatusConnecting() {
            super.onRtspStatusConnecting()
            viewModel.updateStatus(StreamStatus.Connecting)
        }

        override fun onRtspStatusDisconnected() {
            super.onRtspStatusDisconnected()
            viewModel.updateStatus(StreamStatus.Disconnected)
        }

        override fun onRtspStatusFailedUnauthorized() {
            super.onRtspStatusFailedUnauthorized()
            viewModel.updateStatus(StreamStatus.Error(context.getString(R.string.unauthorized)))
        }

        override fun onRtspStatusFailed(message: String?) {
            super.onRtspStatusFailed(message)
            viewModel.updateStatus(StreamStatus.Error(message ?: ""))
        }

        override fun onRtspStatusConnected() {
            super.onRtspStatusConnected()
            viewModel.updateStatus(StreamStatus.Connected)
        }

        override fun onRtspStatusDisconnecting() {
            super.onRtspStatusDisconnecting()
            viewModel.updateStatus(StreamStatus.Disconnecting)
        }
    }

    val streamView: MutableState<RtspSurfaceView?> = remember(context) { mutableStateOf(null) }

    DisposableEffect(key1 = state.currentUri, key2 = streamView) {

        if (state.currentUri.isNotEmpty()) {
            streamView.value?.apply {
                val uri = Uri.parse(state.currentUri)
                init(uri, null, null)
                setStatusListener(listener)
                start(requestVideo = true, requestAudio = false)
            }
        }

        onDispose {
            streamView.value?.stop()
        }
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.statusLog.size) {
                        Text(text = state.statusLog[it].getString())
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.Black),
            contentAlignment = Alignment.Center
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        ratio = 1f,
                        matchHeightConstraintsFirst = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                    )
                    .border(
                        width = 0.1.dp,
                        color = Color.Red
                    ),
                factory = { context ->
                    RtspSurfaceView(context).apply { streamView.value = this }
                }
            )

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
                        TextButton(
                            modifier = Modifier.alpha(0.8f),
                            onClick = { showBottomSheet = true },
                        ) {
                            Text(
                                color = Color.White,
                                text = status
                            )
                        }

                        IconButton(
                            modifier = Modifier.alpha(0.8f),
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


}