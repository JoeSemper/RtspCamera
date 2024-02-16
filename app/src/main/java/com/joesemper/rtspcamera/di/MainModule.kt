package com.joesemper.rtspcamera.di

import com.joesemper.rtspcamera.ui.screens.home.HomeViewModel
import com.joesemper.rtspcamera.ui.screens.stream.StreamViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { HomeViewModel() }
    viewModel { StreamViewModel() }
}