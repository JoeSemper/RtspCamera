package com.joesemper.rtspcamera.data.datastore

import kotlinx.coroutines.flow.Flow

interface RtspDataStore {
    fun getCurrentStreamUri(): Flow<String>
    suspend fun setCurrentStreamUri(uri: String)
}