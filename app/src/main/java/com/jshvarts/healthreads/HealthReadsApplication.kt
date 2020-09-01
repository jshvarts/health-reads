package com.jshvarts.healthreads

import android.app.Application
import com.jshvarts.healthreads.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HealthReadsApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@HealthReadsApplication)
      modules(listOf(dataModule))
    }
  }
}
