package black.bracken.mini_gemini_front.di

import android.content.Context
import androidx.room.Room
import black.bracken.mini_gemini_front.data.infra.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

  @Singleton
  @Provides
  fun provideDatabase(
    @ApplicationContext context: Context,
  ) = Room
    .databaseBuilder(
      context,
      AppDatabase::class.java,
      "app_database",
    ).build()

  @Singleton
  @Provides
  fun provideInteractiveHistoryDao(
    database: AppDatabase,
  ) = database.interactiveHistoryDao()

}