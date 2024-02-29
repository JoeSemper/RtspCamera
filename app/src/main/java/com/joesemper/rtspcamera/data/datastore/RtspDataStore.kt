package com.joesemper.rtspcamera.data.datastore

import com.joesemper.rtspcamera.ui.screens.home.StreamSettings
import kotlinx.coroutines.flow.Flow

interface RtspDataStore {
    fun getCurrentStreamUri(): Flow<String>
    suspend fun setCurrentStreamUri(uri: String)
    fun getCurrentSettings(): Flow<StreamSettings>
    suspend fun updateSettings(newSettings: StreamSettings)
    suspend fun setEnableVideo(enable: Boolean)
    suspend fun setEnableAudio(enable: Boolean)
    suspend fun setUsername(username: String)
    suspend fun setPassword(password: String)
    suspend fun setAspectRatio(ratio: Float)
}