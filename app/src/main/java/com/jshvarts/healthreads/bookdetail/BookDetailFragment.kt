package com.jshvarts.healthreads.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.jshvarts.healthreads.R
import com.jshvarts.healthreads.databinding.FragmentBookDetailBinding
import com.jshvarts.healthreads.domain.Book
import com.jshvarts.healthreads.util.exhaustive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BookDetailFragment : Fragment() {
  private val args by navArgs<BookDetailFragmentArgs>()

  private val viewModel: BookDetailViewModel by viewModel()

  private var _binding: FragmentBookDetailBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedElementEnterTransition = TransitionInflater.from(context)
      .inflateTransition(android.R.transition.move)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    postponeEnterTransition()
    _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.viewState.collect {
        when (it) {
          is DetailViewState.Loading -> renderLoadingState()
          is DetailViewState.Error -> renderErrorState()
          is DetailViewState.Data -> renderDataState(it.book)
        }.exhaustive
      }
    }

    viewModel.getBookDetail(args.isbn)
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  private fun renderLoadingState() {
    binding.loadingIndicator.visibility = View.VISIBLE
  }

  private fun renderErrorState() {
    binding.loadingIndicator.visibility = View.GONE
    Snackbar.make(
      binding.root,
      R.string.error_message,
      Snackbar.LENGTH_LONG
    ).show()
  }

  private fun renderDataState(book: Book) {
    binding.loadingIndicator.visibility = View.GONE

    binding.bookImage.load(book.imageUrl) {
      listener(
        onError = { _, throwable ->
          Timber.e(throwable, "Error downloading book image")
        },
        onSuccess = { _, _ ->
          startPostponedEnterTransition()
        }
      )
    }
    binding.bookImage.transitionName = args.isbn
    binding.bookTitle.text = book.title
    binding.contributor.text = book.contributor
  }
}
