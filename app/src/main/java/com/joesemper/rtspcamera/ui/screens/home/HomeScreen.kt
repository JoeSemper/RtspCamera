package com.joesemper.rtspcamera.ui.screens.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joesemper.rtspcamera.R
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToStream: () -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {

    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    var showEditLinkDialog by remember { mutableStateOf(false) }
    var showEditLinkUsernamePasswordDialog by remember { mutableStateOf(false) }

    if (showEditLinkDialog) {
        SettingsDialog(
            title = stringResource(R.string.change_stream_link),
            firstSubtitle = stringResource(id = R.string.stream_link),
            firstTextDefault = uiState.settings.streamLink,
            onApply = { newLink, _ ->
                viewModel.updateStreamUri(newLink)
            },
            onDismiss = { showEditLinkDialog = false }
        )
    }

    if (showEditLinkUsernamePasswordDialog) {
        SettingsDialog(
            title = stringResource(R.string.change_username_and_password),
            firstSubtitle = stringResource(R.string.username),
            secondSubtitle = stringResource(R.string.password),
            firstTextDefault = uiState.settings.username,
            secondTextDefault = uiState.settings.password,
            onApply = { username, password ->
                viewModel.updateUsername(username)
                viewModel.updatePassword(password)
            },
            onDismiss = { showEditLinkUsernamePasswordDialog = false }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = "Primary",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .clickable { showEditLinkDialog = true }
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.current_link),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = uiState.settings.streamLink.ifEmpty { stringResource(R.string.no_link) }
                            )
                        }

                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }

                    Divider(modifier = Modifier.fillMaxWidth())

                    Column {
                        Row(
                            modifier = Modifier
                                .clickable { showEditLinkUsernamePasswordDialog = true }
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.username),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = uiState.settings.username.ifEmpty { stringResource(R.string.no_username) }
                                )
                            }

                            Icon(
                                modifier = Modifier.padding(start = 8.dp),
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )

                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .clickable { showEditLinkUsernamePasswordDialog = true }
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.password),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = uiState.settings.password.ifEmpty { stringResource(R.string.no_password) }
                                )
                            }

                            Icon(
                                modifier = Modifier.padding(start = 8.dp),
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )

                        }
                    }
                }

            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = "Image",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            text = stringResource(R.string.enable_video),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Checkbox(
                            checked = uiState.settings.enableVideo,
                            onCheckedChange = { viewModel.updateEnableVideo(it) })
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            text = stringResource(R.string.enable_audio),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Checkbox(
                            checked = uiState.settings.enableAudio,
                            onCheckedChange = { viewModel.updateEnableAudio(it) })
                    }

                    Divider(modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                        .fillMaxWidth())

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.aspect_ratio),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AspectRatio.values().forEach { aspectRatio ->
                                FilterChip(
                                    selected = aspectRatio.ratio == uiState.settings.ratio,
                                    onClick = { viewModel.updateRatio(aspectRatio.ratio) },
                                    label = { Text(text = "${aspectRatio.w}:${aspectRatio.h}") }
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = "Other",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .clickable { resetPreferredLauncherAndOpenChooser(context) }
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.set_default_launcher),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }

                    Divider(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth())

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            text = stringResource(R.string.enable_reconnect_timeout),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Checkbox(
                            checked = uiState.settings.enableReconnectTimeout,
                            onCheckedChange = { viewModel.updateEnableReconnectTimeout(it) })
                    }

                }
            }

            Spacer(modifier = Modifier.height(32.dp))

        }
    }

}

@Composable
fun SettingsDialog(
    title: String,
    firstSubtitle: String = "",
    secondSubtitle: String = "",
    firstTextDefault: String = "",
    secondTextDefault: String = "",
    onDismiss: () -> Unit,
    onApply: (text1: String, text2: String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        var text1 by remember { mutableStateOf(firstTextDefault) }
        var text2 by remember { mutableStateOf(secondTextDefault) }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text1,
                    label = {
                        Text(text = firstSubtitle)
                    },
                    singleLine = true,
                    onValueChange = { text1 = it }
                )

                if (secondSubtitle.isNotEmpty()) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = text2,
                        label = {
                            Text(text = secondSubtitle)
                        },
                        singleLine = true,
                        onValueChange = { text2 = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.close))
                    }
                    TextButton(onClick = {
                        onApply(text1, text2)
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.save))
                    }
                }


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

    val selector = Intent(Intent.ACTION_MAIN)
    selector.addCategory(Intent.CATEGORY_HOME)
    selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(selector)

    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        PackageManager.DONT_KILL_APP
    )
}