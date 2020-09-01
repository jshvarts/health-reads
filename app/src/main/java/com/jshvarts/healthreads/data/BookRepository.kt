package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BookRepository(private val api: Api) {
  suspend fun fetchBooks(): Flow<List<Book>> {
    return flow { emit(api.fetchBooks()) }
  }

  suspend fun fetchBook(isbn: String): Flow<Result<Book>> {
    return flow {
      val book = api.fetchBooks().first { it.isbn == isbn }
      emit(Result.success(book))
    }.catch { emit(Result.failure(it)) }
  }
}
