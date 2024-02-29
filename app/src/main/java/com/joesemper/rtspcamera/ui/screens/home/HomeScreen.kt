package com.joesemper.rtspcamera.ui.screens.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.joesemper.rtspcamera.R
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToStream: () -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {

    val context = LocalContext.current

    var uriText by remember { mutableStateOf("") }

    var showEditLinkDialog by remember { mutableStateOf(false) }
    var showEditLinkUsernamePasswordDialog by remember { mutableStateOf(false) }

    if (showEditLinkDialog) {
        Dialog(onDismissRequest = { showEditLinkDialog = false }) {
            var text by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = text,
                    label = {
                        Text(text = "Stream link")
                    },
                    minLines = 3,
                    maxLines = 3,
                    onValueChange = { text = it }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showEditLinkDialog = false }) {
                        Text(text = "Close")
                    }
                    TextButton(onClick = {
                        viewModel.updateStreamUri(text)
                        showEditLinkDialog = false
                    }) {
                        Text(text = "Save")
                    }
                }


            }


        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings))
                },
                actions = {
                    IconButton(onClick = { navigateToStream() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

//            Text(
//                text = "Current link: ${viewModel.uiState.ifEmpty { stringResource(R.string.no_link) }}"
//            )

            Button(onClick = { showEditLinkDialog = true }) {
                Text(text = "Dialog")
            }

            Button(onClick = { resetPreferredLauncherAndOpenChooser(context) }) {
                Text(text = "Set default launcher")
            }

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
                onClick = {
                    navigateToStream()
                }
            ) {
                Text(text = stringResource(R.string.start_stream))
            }
        }
    }


}

private fun resetPreferredLauncherAndOpenChooser(context: Context) {
    val packageManager = context.packageManager
    val componentName = ComponentName(context, com.joesemper.rtspcamera.FakeActivity::class.java)
    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    );

    val selector = Intent(Intent.ACTION_MAIN);
    selector.addCategory(Intent.CATEGORY_HOME);
    selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(selector);

    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        PackageManager.DONT_KILL_APP
    );
}