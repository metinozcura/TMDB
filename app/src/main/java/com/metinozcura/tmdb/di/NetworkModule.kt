package com.metinozcura.tmdb.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.metinozcura.tmdb.BuildConfig
import com.metinozcura.tmdb.configuration.api.TmdbConfigurationApi
import com.metinozcura.tmdb.genres.api.TmdbGenresApi
import com.metinozcura.tmdb.movies.api.TmdbMoviesApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Replace with BuildConfig.TMDB_API_KEY when you add it to your build.
 * This is the TMDB API Read Access Token (static, from API console).
 */
private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

// Note: This should be stored in a secure place in production, like an environment variable in Bitrise
private const val TMDB_API_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0OGY5YmRhOTIwMDU4Y2RkZGJlMjdkNDRjMjI0NTE0YyIsIm5iZiI6MTc3MTY3NTkzMS42MDYwMDAyLCJzdWIiOiI2OTk5YTExYmQwMzFmMTE0ZjgwZTQ5ZGYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.sfnBfXCr825rAj_T3psKEs0MJG3H5w6Rm9Gv9DjNhvI" // TODO move secrets

val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { TmdbApiKeyInterceptor(TMDB_API_KEY) }

    single(createdAtStart = true) {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get<TmdbApiKeyInterceptor>())

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(get<HttpLoggingInterceptor>())
        }
        builder.build()
    }

    single(createdAtStart = true) {
        val json = Json {
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<TmdbMoviesApi> {
        get<Retrofit>().create(TmdbMoviesApi::class.java)
    }

    single<TmdbConfigurationApi> {
        get<Retrofit>().create(TmdbConfigurationApi::class.java)
    }

    single<TmdbGenresApi> {
        get<Retrofit>().create(TmdbGenresApi::class.java)
    }
}

/**
 * Adds the TMDB API Read Access Token to every request as required by TMDB:
 * - Authorization: Bearer &lt;api_key&gt;
 * - accept: application/json
 *
 * Use this client only for TMDB API calls (guest session, movies, etc.).
 */
class TmdbApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", apiKey)
            .addHeader("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}
