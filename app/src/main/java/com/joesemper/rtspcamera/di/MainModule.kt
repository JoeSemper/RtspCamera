package com.joesemper.rtspcamera.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.joesemper.rtspcamera.data.datastore.RtspDataStore
import com.joesemper.rtspcamera.data.datastore.RtspDataStoreImpl
import com.joesemper.rtspcamera.data.datastore.dataStore
import com.joesemper.rtspcamera.ui.screens.home.HomeViewModel
import com.joesemper.rtspcamera.ui.screens.stream.StreamViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { StreamViewModel(get()) }

    single<DataStore<Preferences>> { androidContext().dataStore }
    single<RtspDataStore> { RtspDataStoreImpl(get()) }
}