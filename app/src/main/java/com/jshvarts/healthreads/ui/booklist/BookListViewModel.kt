package com.jshvarts.healthreads.ui.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.ui.ConnectionHelper
import com.jshvarts.healthreads.ui.ErrorType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookListViewModel(
  private val bookRepository: BookRepository,
  private val connectionHelper: ConnectionHelper
) : ViewModel() {

  private val _viewState = MutableLiveData<BookListViewState>()
  val viewState: LiveData<BookListViewState> = _viewState

  fun getBooks(forceRefresh: Boolean) {
    viewModelScope.launch {
      bookRepository.fetchBooks(forceRefresh)
        .onStart {
          _viewState.value = BookListViewState.Loading
        }.catch { throwable ->
          Timber.e(throwable, "error getting book list")
          _viewState.value = if (connectionHelper.isConnected()) {
            BookListViewState.Error(ErrorType.GENERIC)
          } else {
            BookListViewState.Error(ErrorType.CONNECTION)
          }
        }.collect { bookList ->
          _viewState.value = BookListViewState.Data(bookList)
        }
    }
  }
}
