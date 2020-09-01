package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookRepository(private val api: Api) {
  suspend fun fetchBooks(): Flow<List<Book>> {
    return flow { emit(api.fetchBooks()) }
  }

  suspend fun fetchBook(isbn: String): Book {
    return api.fetchBooks().first { it.isbn == isbn }
  }
}
