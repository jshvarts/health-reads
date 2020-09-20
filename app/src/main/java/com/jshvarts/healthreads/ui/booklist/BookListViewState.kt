package com.jshvarts.healthreads.ui.booklist

import com.jshvarts.healthreads.data.model.Book
import com.jshvarts.healthreads.ui.ErrorType

sealed class BookListViewState {
  object Loading : BookListViewState()
  data class Error(val type: ErrorType) : BookListViewState()
  data class Data(val books: List<Book>) : BookListViewState()
}