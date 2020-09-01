package com.jshvarts.healthreads

import android.app.Application
import com.jshvarts.healthreads.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class HealthReadsApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@HealthReadsApplication)
      modules(listOf(dataModule))
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}
