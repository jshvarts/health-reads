package com.jshvarts.healthreads.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.jshvarts.healthreads.databinding.FragmentBookListBinding
import com.jshvarts.healthreads.domain.Book
import org.koin.android.viewmodel.ext.android.viewModel

class BookListFragment : Fragment() {
  private val viewModel: BookListViewModel by viewModel()

  private var _binding: FragmentBookListBinding? = null
  private val binding get() = _binding!!

  private val recyclerViewAdapter = BookListAdapter { book, bookImageView ->
    onBookClicked(book, bookImageView)
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

    viewModel.books.observe(viewLifecycleOwner) {
      recyclerViewAdapter.submitList(it)
    }

    viewModel.getBooks()
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  private fun onBookClicked(book: Book, bookImageView: ImageView) {
    val action = BookListFragmentDirections.actionListToDetail(book.isbn)

    val extras = FragmentNavigatorExtras(
      bookImageView to book.isbn
    )
    findNavController().navigate(action, extras)
  }
}



