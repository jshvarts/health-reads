package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.data.BookRepository
import org.koin.dsl.module

val repoModule = module {
  single { BookRepository(get()) }
}
