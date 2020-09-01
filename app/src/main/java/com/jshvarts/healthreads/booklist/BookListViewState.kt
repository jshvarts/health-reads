package com.jshvarts.healthreads.booklist

import com.jshvarts.healthreads.domain.Book

sealed class BookListViewState {
  object Loading : BookListViewState()
  object Error : BookListViewState()
  data class Data(val books: List<Book>) : BookListViewState()
}