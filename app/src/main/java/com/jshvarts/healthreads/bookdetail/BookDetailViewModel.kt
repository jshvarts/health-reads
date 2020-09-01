package com.jshvarts.healthreads.bookdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.launch

class BookDetailViewModel(
  private val bookRepository: BookRepository
) : ViewModel() {

  private val _book = MutableLiveData<Book>()
  val book: LiveData<Book> = _book

  fun getBookDetail(isbn: String) {
    viewModelScope.launch {
      _book.value = bookRepository.fetchBook(isbn)
    }
  }
}
