package black.bracken.mini_gemini_front.data.infra.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interactive_history")
data class InteractiveHistory(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val speaker: String,
  val content: String,
)