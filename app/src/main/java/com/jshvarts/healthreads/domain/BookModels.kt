package com.jshvarts.healthreads.domain

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * Json payload is as follows:
 *
 * results {
 * --> books [
 * ----> { book1 }
 * ----> { book2 }
 * ----> { bookN }
 */
@JsonClass(generateAdapter = true)
data class Book(
  val title: String,
  @Json(name = "primary_isbn10") val isbn: String,
  @Json(name = "book_image") val imageUrl: String,
  val contributor: String
)

data class BookResults(
  val results: BookList
)

data class BookList(
  val books: List<Book>
)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class WrappedBookList

class BooksJsonAdapter {
  @WrappedBookList
  @FromJson
  fun fromJson(json: BookResults): List<Book> {
    return json.results.books
  }

  @ToJson
  fun toJson(@WrappedBookList value: List<Book>): BookList {
    throw UnsupportedOperationException()
  }
}
