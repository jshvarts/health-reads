package com.jshvarts.healthreads.booklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class BookListViewModel(
  private val bookRepository: BookRepository
) : ViewModel() {

  private val _books = MutableLiveData<List<Book>>()
  val books: LiveData<List<Book>> = _books

  fun getBooks() {
    viewModelScope.launch {
      bookRepository.fetchBooks()
        .onStart {
          Timber.d("onStart")
        }.onCompletion {
          Timber.d("onCompletion")
        }.catch {
          Timber.d("catch error")
        }.collect { bookList ->
          // Timber.d("collected data $bookList")
          _books.value = bookList
        }
    }
  }
}
