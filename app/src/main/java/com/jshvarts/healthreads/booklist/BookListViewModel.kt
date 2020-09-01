package com.jshvarts.healthreads.booklist

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

  private val _books = MutableLiveData<BookListViewState>()
  val books: LiveData<BookListViewState> = _books

  fun getBooks() {
    viewModelScope.launch {
      bookRepository.fetchBooks()
        .onStart {
          _books.value = BookListViewState.Loading
        }.catch {
          Timber.e("error getting book list")
          _books.value = BookListViewState.Error
        }.collect { bookList ->
          _books.value = BookListViewState.Data(bookList)
        }
    }
  }
}
