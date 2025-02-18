package black.bracken.mini_gemini_front.di

import black.bracken.mini_gemini_front.repository.GeminiRepository
import black.bracken.mini_gemini_front.repository.GeminiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun provideGeminiRepository(geminiRepositoryImpl: GeminiRepositoryImpl): GeminiRepository

}