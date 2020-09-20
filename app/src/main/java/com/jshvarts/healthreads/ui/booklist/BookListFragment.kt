package com.jshvarts.healthreads.ui.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jshvarts.healthreads.R
import com.jshvarts.healthreads.databinding.FragmentBookListBinding
import com.jshvarts.healthreads.data.model.Book
import com.jshvarts.healthreads.ui.ErrorType
import com.jshvarts.healthreads.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListFragment : Fragment() {
  private val viewModel: BookListViewModel by viewModel()

  private var _binding: FragmentBookListBinding? = null
  private val binding get() = _binding!!

  private val recyclerViewAdapter = BookListAdapter { book, bookImageView ->
    onBookClicked(book, bookImageView)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentBookListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    postponeEnterTransition()

    binding.bookListRecyclerView.apply {
      adapter = recyclerViewAdapter
    }

    // when LifecycleOwner is destroyed, LiveData cleans up its references
    viewModel.viewState.observe(viewLifecycleOwner) {
      when (it) {
        is BookListViewState.Loading -> renderLoadingState()
        is BookListViewState.Error -> renderErrorState(it.type)
        is BookListViewState.Data -> renderDataState(it.books)
      }.exhaustive
    }

    binding.pullToRefresh.setOnRefreshListener {
      loadBookList(true)
    }

    loadBookList(false)

    binding.bookListRecyclerView.doOnPreDraw {
      // parent has been drawn-we can start transition animation
      startPostponedEnterTransition()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.book_list_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_add -> {
        findNavController().navigate(BookListFragmentDirections.actionListToAddBook())
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  private fun renderLoadingState() {
    binding.pullToRefresh.isRefreshing = true
  }

  private fun renderErrorState(errorType: ErrorType) {
    binding.pullToRefresh.isRefreshing = false

    val errorMessageResId = when (errorType) {
      ErrorType.CONNECTION -> R.string.error_message_offline
      ErrorType.GENERIC -> R.string.generic_error_message
    }

    Snackbar.make(binding.root, errorMessageResId, Snackbar.LENGTH_LONG).show()
  }

  private fun renderDataState(books: List<Book>) {
    binding.pullToRefresh.isRefreshing = false
    recyclerViewAdapter.submitList(books)
  }

  private fun loadBookList(forceRefresh: Boolean) {
    viewModel.getBooks(forceRefresh)
  }

  private fun onBookClicked(book: Book, bookImageView: ImageView) {
    val action = BookListFragmentDirections.actionListToDetail(book.isbn)

    val extras = FragmentNavigatorExtras(
      bookImageView to book.isbn
    )
    findNavController().navigate(action, extras)
  }
}



