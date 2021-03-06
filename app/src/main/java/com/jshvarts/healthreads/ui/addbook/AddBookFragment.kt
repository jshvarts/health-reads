package com.jshvarts.healthreads.ui.addbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jshvarts.healthreads.databinding.FragmentAddBookBinding

class AddBookFragment : Fragment() {

  private var _binding: FragmentAddBookBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddBookBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }
}
