package com.jshvarts.healthreads.data.network

import com.jshvarts.healthreads.BuildConfig
import com.jshvarts.healthreads.data.model.Book
import com.jshvarts.healthreads.data.model.WrappedBookList
import retrofit2.http.GET

private const val API_KEY = BuildConfig.NYT_API_KEY
private const val FETCH_BOOKS_URL = "lists/current/health.json?api-key=$API_KEY"

interface Api {
  @GET(value = FETCH_BOOKS_URL)
  @WrappedBookList
  suspend fun fetchBooks(): List<Book>
}
