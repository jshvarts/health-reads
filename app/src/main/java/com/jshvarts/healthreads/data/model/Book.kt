package com.jshvarts.healthreads.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "book")
data class Book(
  @PrimaryKey
  @Json(name = "primary_isbn10") val isbn: String,
  val title: String,
  @Json(name = "book_image") val imageUrl: String,
  val contributor: String
)
