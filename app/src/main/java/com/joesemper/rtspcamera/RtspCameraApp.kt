package com.joesemper.rtspcamera

import android.app.Application
import com.joesemper.rtspcamera.di.mainModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RtspCameraApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@RtspCameraApp)
            modules(
                mainModule,
            )
        }
    }
}