# Health Reads

![Build CI](https://github.com/jshvarts/health-reads/workflows/Build%20CI/badge.svg)

Simple app that lets you browse Health books available via NYTimes API.

![Sample](docs/video.gif)

## Running the sample
Define `NYT_API_KEY` in ~/.gradle/gradle.properties or in your project root in order to run the app. You can get your own API key [here](https://developer.nytimes.com/apis).

## Comments
* Offline mode with local persistence using Room is implemented. Pull-to-refresh by user initiates data refresh from the network and updating local cache.
* Current implementation does not paginate results. If I was making a production version, I'd probably go with Jetpack Paging library with BoundaryCallbacks.
* I am using Retrofit and its `suspend` implementation already uses `Dispatchers.IO` under the hood. Therefore, I am not explicitly switching to background thread anywhere in the app. I would do that if there was a resource-intensive operation or some additional network calls outside of Retrofit.
* Originally, both `ViewModel`s in the app used `MutableStateFlow` for the view to observe. I ran into some issues unit-testing this implementation, so I made one `ViewModel` being unit-tested use `LiveData` in the meantime.
* I tried handling process death for the `BookDetailViewModel` but ran into issues getting it to work despite the fact that Koin is supposed to support `SavedStateHandle` in `ViewModel`s now.
* The return type of `BookRepository#fetchBook(isbn: String)` could be `Result<Book>` but kotlin `Result` is not supported in function return types yet so I wrapped it in `Flow` (even thought it's effectively a one-shot operation, not a stream).
