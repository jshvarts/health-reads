package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.bookdetail.BookDetailViewModel
import com.jshvarts.healthreads.booklist.BookListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { BookListViewModel(get()) }
  viewModel { BookDetailViewModel(get()) }
}
