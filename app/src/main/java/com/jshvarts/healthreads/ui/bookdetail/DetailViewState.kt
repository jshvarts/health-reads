package com.jshvarts.healthreads.ui.bookdetail

import com.jshvarts.healthreads.domain.Book

sealed class DetailViewState {
  object Loading : DetailViewState()
  object Error : DetailViewState()
  data class Data(val book: Book) : DetailViewState()
}
