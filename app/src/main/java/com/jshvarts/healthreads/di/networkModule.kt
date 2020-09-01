package com.jshvarts.healthreads.di

import com.jshvarts.healthreads.data.Api
import com.jshvarts.healthreads.domain.BooksJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
    retrofit()
  }

  single {
    get<Retrofit>().create(Api::class.java)
  }
}

private fun okHttp(): OkHttpClient {
  val loggingInterceptor = HttpLoggingInterceptor().apply {
    //level = HttpLoggingInterceptor.Level.BODY
  }

  return OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
}

private val moshi = Moshi.Builder()
  .add(BooksJsonAdapter())
  .add(KotlinJsonAdapterFactory())
  .build()

private fun retrofit() = Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .client(okHttp())
  .build()
