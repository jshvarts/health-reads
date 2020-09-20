package com.jshvarts.healthreads.ui.booklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jshvarts.healthreads.data.BookRepository
import com.jshvarts.healthreads.data.model.Book
import com.jshvarts.healthreads.threading.CoroutineTestRule
import com.jshvarts.healthreads.ui.ConnectionHelper
import com.jshvarts.healthreads.ui.ErrorType
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class BookListViewModelTest {
  @get:Rule
  val rule = CoroutineTestRule()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val bookRepository = mock<BookRepository>()

  private val connectionHelper = mock<ConnectionHelper>()

  private val viewStateObserver = mock<Observer<BookListViewState>>()

  private lateinit var bookListViewModel: BookListViewModel

  @Before
  fun setUp() {
    bookListViewModel = BookListViewModel(bookRepository, connectionHelper).apply {
      viewState.observeForever(viewStateObserver)
    }
  }

  @Test
  fun `when books lookup is successful, data view state is observed`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      val book1 = Book(
        title = "book1",
        contributor = "author1",
        imageUrl = "http://www.example.com",
        isbn = "isbn1"
      )

      val book2 = Book(
        title = "book2",
        contributor = "author2",
        imageUrl = "http://www.example.com",
        isbn = "isbn2"
      )

      val expectedViewState = BookListViewState.Data(listOf(book1, book2))

      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks()

      // WHEN
      launch {
        channel.send(listOf(book1, book2))
      }

      bookListViewModel.getBooks(false)

      // THEN
      verify(viewStateObserver).onChanged(expectedViewState)
    }

  @Test
  fun `when error on books lookup occurs and there is connection, generic error view state is observed`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      doReturn(true)
        .whenever(connectionHelper)
        .isConnected()

      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks()

      // WHEN
      launch {
        channel.close(IOException())
      }

      bookListViewModel.getBooks(false)

      // THEN
      verify(viewStateObserver).onChanged(BookListViewState.Error(ErrorType.GENERIC))
    }

  @Test
  fun `when error on books lookup occurs and no connection, connection error view state is observed`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      doReturn(false)
        .whenever(connectionHelper)
        .isConnected()

      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks()

      // WHEN
      launch {
        channel.close(IOException())
      }

      bookListViewModel.getBooks(false)

      // THEN
      verify(viewStateObserver).onChanged(BookListViewState.Error(ErrorType.CONNECTION))
    }

  @Test
  fun `when books are requested, loading view state is observed`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      val book1 = Book(
        title = "book1",
        contributor = "author1",
        imageUrl = "http://www.example.com",
        isbn = "isbn1"
      )

      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks()

      // WHEN
      launch {
        channel.send(listOf(book1))
      }

      bookListViewModel.getBooks(false)

      // THEN
      inOrder(viewStateObserver) {
        verify(viewStateObserver).onChanged(BookListViewState.Loading)
        verify(viewStateObserver).onChanged(BookListViewState.Data(listOf(book1)))
      }
      verifyNoMoreInteractions(viewStateObserver)
    }

  @Test
  fun `when should force refresh, correct repository api is called`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks(forceRefresh = true)

      // WHEN
      launch {
        channel.send(emptyList())
      }

      bookListViewModel.getBooks(forceRefresh = true)

      // THEN
      verify(bookRepository).fetchBooks(forceRefresh = true)
    }

  @Test
  fun `when should not force refresh, correct repository api is called`() =
    rule.dispatcher.runBlockingTest {
      // GIVEN
      val channel = Channel<List<Book>>()
      val flow = channel.consumeAsFlow()

      doReturn(flow)
        .whenever(bookRepository)
        .fetchBooks(forceRefresh = false)

      // WHEN
      launch {
        channel.send(emptyList())
      }

      bookListViewModel.getBooks(forceRefresh = false)

      // THEN
      verify(bookRepository).fetchBooks(forceRefresh = false)
    }
}
