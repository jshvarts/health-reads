package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.data.persistence.BookDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {
  single {
    BookDatabase.buildDatabase(androidApplication())
  }

  single { get<BookDatabase>().bookDao() }

  single { BookRepository(get(), get()) }
}
