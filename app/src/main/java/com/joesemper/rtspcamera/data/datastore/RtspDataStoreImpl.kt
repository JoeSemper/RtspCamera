package com.joesemper.rtspcamera.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joesemper.rtspcamera.ui.screens.home.StreamSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "Datastore"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class RtspDataStoreImpl(private val dataStore: DataStore<Preferences>) : RtspDataStore {

    companion object {

        private val CURRENT_STREAM_URI = stringPreferencesKey("current_stream_uri")
        private const val DEFAULT_CURRENT_STREAM_URI = ""

        private val ENABLE_VIDEO = booleanPreferencesKey("enable_video")
        private const val DEFAULT_ENABLE_VIDEO = true

        private val ENABLE_AUDIO = booleanPreferencesKey("enable_audio")
        private const val DEFAULT_ENABLE_AUDIO = true

        private val USERNAME = stringPreferencesKey("username")
        private const val DEFAULT_USERNAME = ""

        private val PASSWORD = stringPreferencesKey("password")
        private const val DEFAULT_PASSWORD = ""

        private val ASPECT_RATIO = floatPreferencesKey("aspect_ratio")
        private const val DEFAULT_ASPECT_RATIO = 1f

        private val ENABLE_RECONNECT_TIMEOUT = booleanPreferencesKey("enable_reconnect_timeout")
        private const val DEFAULT_ENABLE_RECONNECT_TIMEOUT = false

    }

    override fun getCurrentStreamUri(): Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENT_STREAM_URI] ?: DEFAULT_CURRENT_STREAM_URI
    }

    override suspend fun setCurrentStreamUri(uri: String) {
        dataStore.edit { settings ->
            settings[CURRENT_STREAM_URI] = uri
        }
    }

    override fun getCurrentSettings() = dataStore.data.map { preferences ->
        StreamSettings(
            streamLink = preferences[CURRENT_STREAM_URI] ?: DEFAULT_CURRENT_STREAM_URI,
            enableVideo = preferences[ENABLE_VIDEO] ?: DEFAULT_ENABLE_VIDEO,
            enableAudio = preferences[ENABLE_AUDIO] ?: DEFAULT_ENABLE_AUDIO,
            username = preferences[USERNAME] ?: DEFAULT_USERNAME,
            password = preferences[PASSWORD] ?: DEFAULT_PASSWORD,
            ratio = preferences[ASPECT_RATIO] ?: DEFAULT_ASPECT_RATIO,
            enableReconnectTimeout = preferences[ENABLE_RECONNECT_TIMEOUT]
                ?: DEFAULT_ENABLE_RECONNECT_TIMEOUT
        )
    }

    override suspend fun updateSettings(newSettings: StreamSettings) {
        dataStore.edit { settings ->
            settings[CURRENT_STREAM_URI] = newSettings.streamLink
            settings[ENABLE_VIDEO] = newSettings.enableVideo
            settings[ENABLE_AUDIO] = newSettings.enableAudio
            settings[USERNAME] = newSettings.username
            settings[PASSWORD] = newSettings.password
            settings[ASPECT_RATIO] = newSettings.ratio
            settings[ENABLE_RECONNECT_TIMEOUT] = newSettings.enableReconnectTimeout
        }
    }

    override suspend fun setEnableVideo(enable: Boolean) {
        dataStore.edit { settings ->
            settings[ENABLE_VIDEO] = enable
        }
    }

    override suspend fun setEnableAudio(enable: Boolean) {
        dataStore.edit { settings ->
            settings[ENABLE_AUDIO] = enable
        }
    }

    override suspend fun setUsername(username: String) {
        dataStore.edit { settings ->
            settings[USERNAME] = username
        }
    }

    override suspend fun setPassword(password: String) {
        dataStore.edit { settings ->
            settings[PASSWORD] = password
        }
    }

    override suspend fun setAspectRatio(ratio: Float) {
        dataStore.edit { settings ->
            settings[ASPECT_RATIO] = ratio
        }
    }

    override suspend fun setEnableReconnectTimeout(enable: Boolean) {
        dataStore.edit { settings ->
            settings[ENABLE_RECONNECT_TIMEOUT] = enable
        }
    }
}