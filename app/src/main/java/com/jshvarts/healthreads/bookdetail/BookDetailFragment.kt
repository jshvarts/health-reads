package com.jshvarts.healthreads.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.jshvarts.healthreads.databinding.FragmentBookDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel

class BookDetailFragment : Fragment() {
  private val args by navArgs<BookDetailFragmentArgs>()

  private val viewModel: BookDetailViewModel by viewModel()

  private var _binding: FragmentBookDetailBinding? = null
  private val binding get() = _binding!!

  private val imageLoadingCallback = object : Callback {
    override fun onSuccess() {
      startPostponedEnterTransition()
    }

    override fun onError(e: Exception?) {
      // no-op
    }
  }

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

    viewModel.book.observe(viewLifecycleOwner) { book ->
      Picasso.get()
        .load(book.imageUrl)
        .into(binding.bookImage, imageLoadingCallback)
      binding.bookImage.transitionName = args.isbn
      binding.bookTitle.text = book.title
      binding.contributor.text = book.contributor
    }

    viewModel.getBookDetail(args.isbn)
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }
}
