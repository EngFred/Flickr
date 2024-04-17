package net.android.app.flickr.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.android.app.flickr.domain.modal.SearchQuery

@Dao
interface QueriesDao {

    @Upsert
    suspend fun addQuery( searchQuery: SearchQuery)

    @Delete
    suspend fun deleteQuery( searchQuery: SearchQuery )

    @Query("Select * From searchquery Order By timestamp Desc Limit 10")
    fun getUserQueries() :  Flow<List<SearchQuery>>

    @Query("SELECT * FROM searchquery WHERE queryText LIKE '%' || :searchText || '%' ORDER BY timestamp DESC LIMIT 10")
    fun searchQueries( searchText: String ): Flow<List<SearchQuery>>

    @Query("SELECT * FROM searchquery WHERE queryText = :queryText")
    fun getQueryByText(queryText: String): Flow<SearchQuery?>

}