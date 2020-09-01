package com.jshvarts.healthreads.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jshvarts.healthreads.R
import com.jshvarts.healthreads.databinding.FragmentBookListBinding
import com.jshvarts.healthreads.domain.Book
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListFragment : Fragment() {
  private val viewModel: BookListViewModel by viewModel()

  private var _binding: FragmentBookListBinding? = null
  private val binding get() = _binding!!

  private val recyclerViewAdapter = BookListAdapter { book, bookImageView ->
    onBookClicked(book, bookImageView)
  }

  private val refreshAfterErrorListener = View.OnClickListener {
    loadBookList()
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

    binding.bookListRecyclerView.apply {
      adapter = recyclerViewAdapter
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.viewState.collect {
        when (it) {
          is BookListViewState.Loading -> renderLoadingState()
          is BookListViewState.Error -> renderErrorState()
          is BookListViewState.Data -> renderDataState(it.books)
        }
      }
    }

    binding.pullToRefresh.setOnRefreshListener {
      loadBookList()
    }

    loadBookList()
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  private fun renderLoadingState() {
    binding.pullToRefresh.isRefreshing = true
  }

  private fun renderErrorState() {
    binding.pullToRefresh.isRefreshing = false
    Snackbar.make(
      binding.root,
      R.string.error_message,
      Snackbar.LENGTH_INDEFINITE
    ).setAction(R.string.refresh_button_text, refreshAfterErrorListener)
      .show()
  }

  private fun renderDataState(books: List<Book>) {
    binding.pullToRefresh.isRefreshing = false
    recyclerViewAdapter.submitList(books)
  }

  private fun loadBookList() {
    viewModel.getBooks()
  }

  private fun onBookClicked(book: Book, bookImageView: ImageView) {
    val action = BookListFragmentDirections.actionListToDetail(book.isbn)

    val extras = FragmentNavigatorExtras(
      bookImageView to book.isbn
    )
    findNavController().navigate(action, extras)
  }
}



