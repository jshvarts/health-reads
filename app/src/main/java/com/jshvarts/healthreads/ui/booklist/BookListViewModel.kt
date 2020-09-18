package com.jshvarts.healthreads.ui.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookListViewModel(
  private val bookRepository: BookRepository
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
          _viewState.value = BookListViewState.Error
        }.collect { bookList ->
          _viewState.value = BookListViewState.Data(bookList)
        }
    }
  }
}
