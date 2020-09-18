package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.ui.ConnectionHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val uiHelperModule = module {
  single { ConnectionHelper(androidApplication()) }
}
