package com.jshvarts.healthreads.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jshvarts.healthreads.data.model.Book

private const val DATABASE_VERSION = 1

@Database(
  entities = [Book::class],
  version = DATABASE_VERSION,
  exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
  companion object {
    private const val DATABASE_NAME = "HealthBooks"

    fun buildDatabase(context: Context): BookDatabase {
      return Room.databaseBuilder(
        context,
        BookDatabase::class.java,
        DATABASE_NAME
      ).build()
    }
  }

  abstract fun bookDao(): BookDao
}