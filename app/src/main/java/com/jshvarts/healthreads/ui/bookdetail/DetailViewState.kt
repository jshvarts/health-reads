package com.jshvarts.healthreads.ui.bookdetail

import com.jshvarts.healthreads.data.model.Book
import com.jshvarts.healthreads.ui.ErrorType

sealed class DetailViewState {
  object Loading : DetailViewState()
  data class Error(val type: ErrorType) : DetailViewState()
  data class Data(val book: Book) : DetailViewState()
}
