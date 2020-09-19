package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.data.network.Api
import com.jshvarts.healthreads.data.persistence.BookDao
import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BookRepository(
  private val api: Api,
  private val bookDao: BookDao
) {
  suspend fun fetchBooks(forceRefresh: Boolean = false): Flow<List<Book>> {
    return flow { emit(loadBooks(forceRefresh)) }
  }

  suspend fun fetchBook(isbn: String, forceRefresh: Boolean = false): Flow<Result<Book>> {
    return flow {
      // unfortunately, NYTimes does not provide a feed to load a single book by isbn or some id.
      // so we fetch all books again and filter the response by the isbn needed
      val book = loadBooks(forceRefresh).first { it.isbn == isbn }
      emit(Result.success(book))
    }.catch { emit(Result.failure(it)) }
  }

  private suspend fun loadBooks(forceRefresh: Boolean): List<Book> {
    return if (forceRefresh) {
      refreshAndCache()
    } else {
      bookDao.fetchBooks().also {
        if (it.isEmpty()) {
          return refreshAndCache()
        }
      }
    }
  }

  private suspend fun refreshAndCache(): List<Book> {
    return api.fetchBooks().also {
      if (it.isNotEmpty()) {
        bookDao.insertBooks(it)
      }
    }
  }
}
