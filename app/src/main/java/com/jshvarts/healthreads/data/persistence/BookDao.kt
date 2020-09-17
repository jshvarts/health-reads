package com.jshvarts.healthreads.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jshvarts.healthreads.domain.Book

@Dao
interface BookDao {
  @Query("SELECT * FROM book")
  suspend fun fetchBooks(): List<Book>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooks(books: List<Book>)
}
