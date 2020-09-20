package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.data.network.Api
import com.jshvarts.healthreads.data.persistence.BookDao
import com.jshvarts.healthreads.data.model.Book
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val TEST_ISBN = "isbn1"

class BookRepositoryTest {

  @Test
  fun `when book detail lookup succeeds, success result is emitted`() = runBlocking {
    // GIVEN
    val book1 = Book(
      title = "book1",
      contributor = "author1",
      imageUrl = "http://www.example.com",
      isbn = TEST_ISBN
    )

    val api = mock<Api> {
      onBlocking { fetchBooks() } doReturn emptyList()
    }

    val bookDao = mock<BookDao> {
      onBlocking { fetchBooks() } doReturn listOf(book1)
    }

    val repository = BookRepository(api, bookDao)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN)

    // THEN
    flow.collect { result: Result<Book> ->
      assertTrue { result.isSuccess }
      assertEquals(book1, result.getOrNull())
    }
  }

  @Test
  fun `when book detail lookup fails, error result is emitted`() = runBlocking {
    // GIVEN
    val api = mock<Api> {
      onBlocking { fetchBooks() } doAnswer { throw IOException() }
    }

    val bookDao = mock<BookDao> {
      onBlocking { fetchBooks() } doReturn emptyList()
    }

    val repository = BookRepository(api, bookDao)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN)

    // THEN
    flow.collect { result: Result<Book> ->
      assertTrue { result.isFailure }
    }
  }

  @Test
  fun `when should not force refresh and local data present, returns local data`() =
    runBlockingTest {
      // GIVEN
      val book1 = Book(
        title = "book1",
        contributor = "author1",
        imageUrl = "http://www.example.com",
        isbn = TEST_ISBN
      )

      val bookDao = mock<BookDao> {
        onBlocking { fetchBooks() } doReturn listOf(book1)
      }

      val api = mock<Api> {
        onBlocking { fetchBooks() } doReturn emptyList()
      }

      val repository = BookRepository(api, bookDao)

      // WHEN
      val flow = repository.fetchBook(TEST_ISBN, forceRefresh = false)

      // THEN
      flow.collect { result: Result<Book> ->
        assertTrue { result.isSuccess }
        assertEquals(book1, result.getOrNull())
      }
      verify(api, never()).fetchBooks()
    }

  @Test
  fun `when should not force refresh and local data not present, gets remote data and caches it`() =
    runBlockingTest {
      // GIVEN
      val book1 = Book(
        title = "book1",
        contributor = "author1",
        imageUrl = "http://www.example.com",
        isbn = TEST_ISBN
      )

      val bookDao = mock<BookDao> {
        onBlocking { fetchBooks() } doReturn emptyList()
      }

      val api = mock<Api> {
        onBlocking { fetchBooks() } doReturn listOf(book1)
      }

      val repository = BookRepository(api, bookDao)

      // WHEN
      val flow = repository.fetchBook(TEST_ISBN, forceRefresh = false)

      // THEN
      flow.collect { result: Result<Book> ->
        assertTrue { result.isSuccess }
        assertEquals(book1, result.getOrNull())
      }

      inOrder(bookDao, api) {
        verify(bookDao).fetchBooks()
        verify(api).fetchBooks()
        verify(bookDao).insertBooks(listOf(book1))
      }
    }

  @Test
  fun `when should force refresh, gets remote data and caches it`() = runBlockingTest {
    // GIVEN
    val book1 = Book(
      title = "book1",
      contributor = "author1",
      imageUrl = "http://www.example.com",
      isbn = TEST_ISBN
    )

    val bookDao = mock<BookDao> {
      onBlocking { fetchBooks() } doReturn emptyList()
    }

    val api = mock<Api> {
      onBlocking { fetchBooks() } doReturn listOf(book1)
    }

    val repository = BookRepository(api, bookDao)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN, forceRefresh = true)

    // THEN
    flow.collect { result: Result<Book> ->
      assertTrue { result.isSuccess }
      assertEquals(book1, result.getOrNull())
    }

    verify(bookDao, never()).fetchBooks()

    inOrder(bookDao, api) {
      verify(api).fetchBooks()
      verify(bookDao).insertBooks(listOf(book1))
    }
  }
}