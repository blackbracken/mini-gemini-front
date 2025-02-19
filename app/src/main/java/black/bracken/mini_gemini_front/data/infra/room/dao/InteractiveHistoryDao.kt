package black.bracken.mini_gemini_front.data.infra.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import black.bracken.mini_gemini_front.data.infra.room.entity.InteractiveHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractiveHistoryDao {

  @Insert
  suspend fun insert(history: InteractiveHistory)

  @Query("DELETE FROM interactive_history")
  suspend fun deleteAll()

  @Query("SELECT * FROM interactive_history ORDER BY id")
  suspend fun getAll(): List<InteractiveHistory>

  @Query("SELECT * FROM interactive_history ORDER BY id")
  fun getAllFlow(): Flow<List<InteractiveHistory>>

}