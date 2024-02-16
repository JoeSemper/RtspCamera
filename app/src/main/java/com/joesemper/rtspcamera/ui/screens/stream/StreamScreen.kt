package com.joesemper.rtspcamera.ui.screens.stream

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.alexvas.rtsp.widget.RtspSurfaceView
import org.koin.androidx.compose.getViewModel

@Composable
fun StreamScreen(
    navigateHome: () -> Unit,
    viewModel: StreamViewModel = getViewModel()
) {

    val uri = remember(viewModel.uiState) {
        Uri.parse(viewModel.uiState)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier.alpha(0.4f),
            onClick = { navigateHome() }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.size(200.dp),
                factory = { context ->
                    RtspSurfaceView(context).apply {
                        init(uri, null, null)
                        start(requestVideo = true, requestAudio = false)
                    }
                }
            )
        }
    }


}