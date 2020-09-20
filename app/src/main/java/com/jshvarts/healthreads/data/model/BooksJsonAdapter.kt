package com.jshvarts.healthreads.data.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * We are using Moshi Codegen and rely on custom adapters to parse json.
 *
 * Json payload is as follows:
 *
 * results { -> BookResults
 * --> books [ -> BookList
 * ----> { book1 } -> Book
 * ----> { book2 } -> Book
 * ----> { bookN } -> Book
 */

@JsonClass(generateAdapter = true)
data class BookResults(
  val results: BookList
)

@JsonClass(generateAdapter = true)
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
