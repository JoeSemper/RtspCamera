package com.joesemper.rtspcamera.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "Datastore"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class RtspDataStoreImpl(private val dataStore: DataStore<Preferences>) : RtspDataStore {

    companion object {

        private val CURRENT_STREAM_URI = stringPreferencesKey("current_stream_uri")
        private const val DEFAULT_CURRENT_STREAM_URI = ""

    }

    override fun getCurrentStreamUri(): Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENT_STREAM_URI] ?: DEFAULT_CURRENT_STREAM_URI
    }

    override suspend fun setCurrentStreamUri(uri: String) {
        dataStore.edit { settings ->
            settings[CURRENT_STREAM_URI] = uri
        }
    }
}