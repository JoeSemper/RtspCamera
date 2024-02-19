package com.joesemper.rtspcamera.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joesemper.rtspcamera.R
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    navigateToStream: () -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {

    var uriText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Current link: ${viewModel.uiState.ifEmpty { stringResource(R.string.no_link) }}"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "New link")
                },
                value = uriText,
                onValueChange = { uriText = it }
            )

            Button(
                modifier = Modifier,
                onClick = {
                    viewModel.updateStreamUri(uriText)
                    uriText = ""
                }
            ) {
                Text(text = "Update")
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = viewModel.uiState.isNotEmpty(),
            onClick = {
                navigateToStream()
            }
        ) {
            Text(text = stringResource(R.string.start_stream))
        }
    }
}