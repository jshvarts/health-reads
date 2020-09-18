package com.jshvarts.healthreads.ui.booklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jshvarts.healthreads.databinding.ItemBookBinding
import com.jshvarts.healthreads.domain.Book

class BookListAdapter(
  private val clickListener: (Book, ImageView) -> Unit
) : ListAdapter<Book, BookViewHolder>(BookDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, position: Int): BookViewHolder {
    val binding = ItemBookBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return BookViewHolder(binding, clickListener)
  }

  override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class BookViewHolder(
  binding: ItemBookBinding,
  private val clickListener: (Book, ImageView) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

  private val bookTitleView = binding.bookTitle
  private val bookImageView = binding.bookImage
  private val cardView = binding.root

  fun bind(item: Book) {

    bookImageView.apply {
      load(item.imageUrl) {
        crossfade(true)
      }
      transitionName = item.isbn
    }
    bookTitleView.text = item.title
    cardView.setOnClickListener {
      clickListener.invoke(item, bookImageView)
    }
  }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
  override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
    return oldItem == newItem
  }

  override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
    return oldItem.isbn == newItem.isbn
  }
}
