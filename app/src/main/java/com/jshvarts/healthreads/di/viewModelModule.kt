package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.ui.bookdetail.BookDetailViewModel
import com.jshvarts.healthreads.ui.booklist.BookListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { BookListViewModel(get(), get()) }
  viewModel { BookDetailViewModel(get(), get()) }
}
