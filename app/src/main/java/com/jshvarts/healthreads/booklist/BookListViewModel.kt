package com.jshvarts.healthreads.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.bookdetail.DetailViewState
import com.jshvarts.healthreads.data.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookListViewModel(
  private val bookRepository: BookRepository
) : ViewModel() {

  private val _viewState = MutableStateFlow<BookListViewState>(BookListViewState.Loading)
  val viewState: StateFlow<BookListViewState> = _viewState

  fun getBooks() {
    viewModelScope.launch {
      bookRepository.fetchBooks()
        .onStart {
          _viewState.value = BookListViewState.Loading
        }.catch {
          Timber.e("error getting book list")
          _viewState.value = BookListViewState.Error
        }.collect { bookList ->
          _viewState.value = BookListViewState.Data(bookList)
        }
    }
  }
}
