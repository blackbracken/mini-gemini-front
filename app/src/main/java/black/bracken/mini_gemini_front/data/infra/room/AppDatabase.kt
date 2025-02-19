package black.bracken.mini_gemini_front.data.infra.room

import androidx.room.Database
import androidx.room.RoomDatabase
import black.bracken.mini_gemini_front.data.infra.room.dao.InteractiveHistoryDao
import black.bracken.mini_gemini_front.data.infra.room.entity.InteractiveHistory

@Database(
  entities = [
    InteractiveHistory::class,
  ],
  version = 1,
  exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun interactiveHistoryDao(): InteractiveHistoryDao
}