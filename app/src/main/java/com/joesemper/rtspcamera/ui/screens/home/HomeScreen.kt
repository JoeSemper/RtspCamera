package com.joesemper.rtspcamera.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.padding(16.dp),
            label = {
                Text(text = stringResource(R.string.stream_link))
            },
            value = uriText,
            onValueChange = { uriText = it }
        )

        Button(
            onClick = {
                viewModel.updateStreamUri(uriText)
                navigateToStream()
            }
        ) {
            Text(text = stringResource(R.string.start_stream))
        }
    }
}