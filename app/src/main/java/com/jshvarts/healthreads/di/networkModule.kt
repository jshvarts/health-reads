package com.jshvarts.healthreads.di

import android.app.Application
import com.jshvarts.healthreads.data.Api
import com.jshvarts.healthreads.domain.BooksJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.nytimes.com/svc/books/v3/"

private const val CACHE_CONTROL_HEADER = "Cache-Control"
private const val CACHE_SIZE = 5 * 1024 * 1024L // 5 MB

val networkModule = module {
  single {
    httpCache(this.androidApplication())
  }

  single {
    okHttp(get())
  }

  single {
    retrofit(get())
  }

  single {
    get<Retrofit>().create(Api::class.java)
  }
}

private fun okHttp(cache: Cache): OkHttpClient {
  return OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor())
    .addNetworkInterceptor(cacheInterceptor())
    .cache(cache)
    .build()
}

private val moshi = Moshi.Builder()
  .add(BooksJsonAdapter())
  .add(KotlinJsonAdapterFactory())
  .build()

private fun retrofit(cache: Cache) = Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .client(okHttp(cache))
  .build()

private fun httpCache(application: Application): Cache {
  return Cache(application.applicationContext.cacheDir, CACHE_SIZE)
}

private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
  level = HttpLoggingInterceptor.Level.HEADERS
}

private fun cacheInterceptor(): Interceptor {
  return Interceptor { chain ->
    val originalResponse = chain.proceed(chain.request())

    val cacheControl = CacheControl.Builder()
      .maxAge(10, TimeUnit.MINUTES)
      .build()
    originalResponse.newBuilder()
      .header(CACHE_CONTROL_HEADER, cacheControl.toString())
      .build()
  }
}