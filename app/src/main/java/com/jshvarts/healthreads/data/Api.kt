package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.BuildConfig
import com.jshvarts.healthreads.domain.Book
import com.jshvarts.healthreads.domain.WrappedBookList
import retrofit2.http.GET
import retrofit2.http.Headers

const val CACHE_CONTROL_HEADER = "Cache-Control"
const val CACHE_CONTROL_NO_CACHE = "no-cache"

private const val API_KEY = BuildConfig.NYT_API_KEY
private const val FETCH_BOOKS_URL = "lists/current/health.json?api-key=$API_KEY"

interface Api {
  @GET(value = FETCH_BOOKS_URL)
  @WrappedBookList
  suspend fun fetchBooks(): List<Book>

  @GET(value = FETCH_BOOKS_URL)
  @WrappedBookList
  @Headers("$CACHE_CONTROL_HEADER: $CACHE_CONTROL_NO_CACHE")
  suspend fun fetchBooksForceRefresh(): List<Book>
}
