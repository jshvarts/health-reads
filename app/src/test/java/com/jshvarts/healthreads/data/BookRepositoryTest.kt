package com.jshvarts.healthreads.data

import com.jshvarts.healthreads.domain.Book
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
      onBlocking { fetchBooks() } doReturn listOf(book1)
    }

    val repository = BookRepository(api)

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

    val repository = BookRepository(api)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN)

    // THEN
    flow.collect { result: Result<Book> ->
      assertTrue { result.isFailure }
    }
  }

  @Test
  fun `when should force refresh, correct network api is called`() = runBlockingTest {
    // GIVEN
    val api = mock<Api> {
      onBlocking { fetchBooks() } doReturn emptyList()
    }

    val repository = BookRepository(api)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN, forceRefresh = true)
    flow.collect()

    // THEN
    verify(api).fetchBooksForceRefresh()
  }

  @Test
  fun `when should not force refresh, correct network api is called`() = runBlockingTest {
    // GIVEN
    val api = mock<Api> {
      onBlocking { fetchBooks() } doReturn emptyList()
    }

    val repository = BookRepository(api)

    // WHEN
    val flow = repository.fetchBook(TEST_ISBN)
    flow.collect()

    // THEN
    verify(api).fetchBooks()
  }
}