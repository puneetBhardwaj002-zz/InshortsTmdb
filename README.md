# InshortsTmdb
Movies listing app built using MVVM, Dagger2 , RxJava2, Room

# Architecture and Tech-stack
1. Built on MVVM architecture pattern
2. Uses Android Architecture Components, specifically ViewModel, LiveData and Room.
3. Heavily uses RxJava for network calls, transformations, and database observation.
4. Offline ready. MovieDB uses Room for managing a local SQLite database, which means that if you have seen some content already while you were online, you won't need an internet connection to see it again. Everything except searched movies are cached.
5. Uses Retrofit for making API calls.
6. Uses Glide for image loading.

# Features
1. Discover Trending and Now playing movies on TMDb.
2. Search for movies
3. View movie details like release date, rating, overview, budget.
4. Works offline by caching data into a database.
