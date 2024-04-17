package net.android.app.flickr.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.android.app.flickr.data.local.db.QueriesDao
import net.android.app.flickr.data.local.db.QueriesDatabase
import net.android.app.flickr.data.local.db.QueriesDatabase.Companion.DATABASE_NAME
import net.android.app.flickr.data.local.preferences.PreferencesRepository
import net.android.app.flickr.data.local.repository.QueriesRepositoryImpl
import net.android.app.flickr.data.remote.repository.FlickrRepositoryImpl
import net.android.app.flickr.data.remote.service.FlickrAPI
import net.android.app.flickr.data.remote.service.PhotoInterceptor
import net.android.app.flickr.data.remote.utils.ApiConstants
import net.android.app.flickr.domain.modal.SearchQuery
import net.android.app.flickr.domain.repository.FlickrRepository
import net.android.app.flickr.domain.repository.QueriesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesFlickrApiInstance() : FlickrAPI {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(PhotoInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(FlickrAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesFlickrRepository( flickrAPI: FlickrAPI) : FlickrRepository = FlickrRepositoryImpl( flickrAPI )

    @Singleton
    @Provides
    fun providesQueriesDatabase( @ApplicationContext context: Context) : QueriesDatabase =
        Room.databaseBuilder(
            context,
            QueriesDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideToDoDao(queriesDatabase: QueriesDatabase) : QueriesDao =
        queriesDatabase.queriesDao

    @Singleton
    @Provides
    fun providesQueriesRepository( queriesDao: QueriesDao ) : QueriesRepository = QueriesRepositoryImpl(queriesDao)

    @Singleton
    @Provides
    fun provideDatastoreInstance(  @ApplicationContext context : Context  ) = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }

    @Singleton
    @Provides
    fun providesPreferencesRepository(  dataStore: DataStore<Preferences> ) : PreferencesRepository = PreferencesRepository( dataStore )

}