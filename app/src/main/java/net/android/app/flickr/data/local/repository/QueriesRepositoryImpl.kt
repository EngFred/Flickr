package net.android.app.flickr.data.local.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import net.android.app.flickr.data.local.db.QueriesDao
import net.android.app.flickr.domain.modal.SearchQuery
import net.android.app.flickr.domain.repository.QueriesRepository
import javax.inject.Inject

class QueriesRepositoryImpl @Inject constructor(
    private val queriesDao: QueriesDao
) : QueriesRepository {


    companion object {
        private const val TAG = "QueriesRepositoryImpl"
    }

    override fun getUserQueries(): Flow<List<SearchQuery>> {
        return channelFlow {
            queriesDao.getUserQueries().collectLatest {
                send(it)
            }
        }.catch {
            Log.d(TAG, "$it")
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteQuery(searchQuery: SearchQuery) {
        try {
            queriesDao.deleteQuery( searchQuery )
            Log.d(TAG, "Query deleted successful in cache!!")
        }catch (ex: Exception){
            Log.d(TAG, "$ex")
        }
    }

    override suspend fun addQuery(searchQuery: SearchQuery) {
        try {
            queriesDao.addQuery( searchQuery )
            Log.d(TAG, "Query added successful in cache!!")
        }catch (ex: Exception){
            Log.d(TAG, "$ex")
        }
    }

    override fun searchQuery(queryText: String): Flow<List<SearchQuery>> {
        return channelFlow {
            queriesDao.searchQueries( queryText ).collectLatest {
                send(it)
            }
        }.catch {
            Log.d(TAG, "$it")
        }.flowOn(Dispatchers.IO)
    }

    override fun getQueryByText(queryText: String): Flow<SearchQuery?> {
        return channelFlow {
            queriesDao.getQueryByText( queryText ).collectLatest {
                send(it)
            }
        }.catch {
            Log.d(TAG, "$it")
        }.flowOn(Dispatchers.IO)
    }
}