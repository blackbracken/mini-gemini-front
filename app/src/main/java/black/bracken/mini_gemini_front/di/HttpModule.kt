package black.bracken.mini_gemini_front.di

import black.bracken.mini_gemini_front.util.mgfJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

@Module
@InstallIn(SingletonComponent::class)
class HttpModule {

  @Provides
  fun provideHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
      install(ContentNegotiation) {
        json(mgfJson)
      }
      install(HttpTimeout) {
        socketTimeoutMillis = 30_000L
      }
    }
  }

}