package com.simplepeople.watcha.data.modules

import com.simplepeople.watcha.BuildConfig
import com.simplepeople.watcha.data.services.TmdbApiService
import com.simplepeople.watcha.data.services.TmdbApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetroFitModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor())
            .build()
    }

    @Provides
    fun provideRetroFitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TmdbApiUrl.BASE_URL.url)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }

    class APIKeyInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val currentUrl = chain.request().url
            val newUrl =
                currentUrl.newBuilder().addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()
            val currentRequest = chain.request().newBuilder()
            val newRequest = currentRequest.url(newUrl).build()
            return chain.proceed(newRequest)
        }

    }

}