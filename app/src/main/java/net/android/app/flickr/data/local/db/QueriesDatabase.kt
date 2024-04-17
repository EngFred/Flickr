package net.android.app.flickr.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.android.app.flickr.domain.modal.SearchQuery

@Database(
    entities = [SearchQuery::class],
    version = 1,
    exportSchema = false
)
abstract class QueriesDatabase : RoomDatabase() {
    abstract val queriesDao: QueriesDao
    companion object {
        const val DATABASE_NAME = "queries.db"
    }
}