package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.data.network.Api
import com.jshvarts.healthreads.domain.BooksJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.nytimes.com/svc/books/v3/"

val networkModule = module {

  single {
    okHttp()
  }

  single {
    retrofit(get())
  }

  single {
    get<Retrofit>().create(Api::class.java)
  }
}

private fun okHttp(): OkHttpClient {
  return OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor())
    .build()
}

private val moshi = Moshi.Builder()
  .add(BooksJsonAdapter())
  .build()

private fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .client(okHttpClient)
  .build()

private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
  level = HttpLoggingInterceptor.Level.HEADERS
}