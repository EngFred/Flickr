package net.android.app.flickr.domain.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchQuery(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val queryText: String,
    val timestamp: Long = System.currentTimeMillis()
)
