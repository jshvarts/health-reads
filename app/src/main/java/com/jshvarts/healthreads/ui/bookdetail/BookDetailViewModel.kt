package com.jshvarts.healthreads.ui.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.ui.ConnectionHelper
import com.jshvarts.healthreads.ui.ErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookDetailViewModel(
  private val bookRepository: BookRepository,
  private val connectionHelper: ConnectionHelper
) : ViewModel() {

  private val _viewState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
  val viewState: StateFlow<DetailViewState> = _viewState

  fun getBookDetail(isbn: String, forceRefresh: Boolean) {
    viewModelScope.launch {

      bookRepository.fetchBook(isbn, forceRefresh)
        .onStart {
          _viewState.value = DetailViewState.Loading
        }
        .collect { result ->
          when {
            result.isSuccess -> {
              _viewState.value = DetailViewState.Data(result.getOrThrow())
            }
            result.isFailure -> {
              Timber.e(result.exceptionOrNull(), "error getting book detail")
              _viewState.value = if (connectionHelper.isConnected()) {
                DetailViewState.Error(ErrorType.GENERIC)
              } else {
                DetailViewState.Error(ErrorType.CONNECTION)
              }
            }
          }
        }
    }
  }
}
