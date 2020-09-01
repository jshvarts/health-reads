package com.jshvarts.healthreads.bookdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookDetailViewModel(
  private val bookRepository: BookRepository
) : ViewModel() {

  private val _book = MutableLiveData<DetailViewState>()
  val book: LiveData<DetailViewState> = _book

  fun getBookDetail(isbn: String) {
    viewModelScope.launch {

      bookRepository.fetchBook(isbn)
        .onStart {
          _book.value = DetailViewState.Loading
        }
        .collect { result ->
          when {
            result.isSuccess -> {
              _book.value = DetailViewState.Data(result.getOrThrow())
            }
            result.isFailure -> {
              Timber.e(result.exceptionOrNull(), "error getting book detail")
              _book.value = DetailViewState.Error
            }
          }
        }
    }
  }
}
