package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BookRepository(private val api: Api) {
  suspend fun fetchBooks(forceRefresh: Boolean = false): Flow<List<Book>> {
    return flow {
      emit(callApi(forceRefresh))
    }
  }

  suspend fun fetchBook(isbn: String, forceRefresh: Boolean = false): Flow<Result<Book>> {
    return flow {
      val book = callApi(forceRefresh).first { it.isbn == isbn }
      emit(Result.success(book))
    }.catch { emit(Result.failure(it)) }
  }

  private suspend fun callApi(forceRefresh: Boolean): List<Book> {
    return if (forceRefresh) {
      api.fetchBooksForceRefresh()
    } else {
      api.fetchBooks()
    }
  }
}
