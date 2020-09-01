package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.BuildConfig
import com.jshvarts.healthreads.domain.Book
import com.jshvarts.healthreads.domain.WrappedBookList
import retrofit2.http.GET

private const val API_KEY = BuildConfig.NYT_API_KEY

interface Api {
  @GET(value = "lists/current/health.json?api-key=$API_KEY")
  @WrappedBookList
  suspend fun fetchBooks(): List<Book>
}
