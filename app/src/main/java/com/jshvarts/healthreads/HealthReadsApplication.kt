package com.jshvarts.healthreads

import android.app.Application
import com.jshvarts.healthreads.di.networkModule
import com.jshvarts.healthreads.di.repoModule
import com.jshvarts.healthreads.di.uiHelperModule
import com.jshvarts.healthreads.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree


class HealthReadsApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger(Level.DEBUG)
      androidContext(this@HealthReadsApplication)
      modules(listOf(repoModule, networkModule, viewModelModule, uiHelperModule))
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}
