package com.jshvarts.healthreads.bookdetail

import com.jshvarts.healthreads.domain.Book

sealed class DetailViewState {
  object Loading : DetailViewState()
  object Error : DetailViewState()
  data class Data(val book: Book) : DetailViewState()
}
