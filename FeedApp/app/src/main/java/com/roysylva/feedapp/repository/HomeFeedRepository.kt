package com.roysylva.feedapp.repository


import android.util.Log
import com.roysylva.feedapp.datasource.local.FeedEntity
import com.roysylva.feedapp.datasource.local.LocalDataSource
import com.roysylva.feedapp.datasource.network.ApiInterface
import com.roysylva.feedapp.helper.Constants
import com.roysylva.feedapp.model.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject


class HomeFeedRepository
@Inject constructor(private val localDataSource: LocalDataSource,private val apiInterface: ApiInterface){


    //in this section we make requests from our local database to confirm if its empty

    suspend fun getHomeFeedResponse(): Flow<Result<FeedEntity>> {

        return flow {
            emit(Result.success( localDataSource.ensureIsNotEmpty().all()))
        }.catch {
            emit(Result.failure(RuntimeException(Constants.RUNTIME_EXCEPTION_MSG)))
        }
    }

    //if the database is empty, home feed is saved on the database

    private suspend fun LocalDataSource.ensureIsNotEmpty() = apply {
        if (isEmpty()){
            val feedEntity = FeedEntity(apiInterface.getHomeFeed().page)
            Log.e("FEED",feedEntity.toString())
            save(feedEntity)
        }
    }

    //for testing purposes only

    suspend fun getAllFeedFromApi(): Flow<Result<FeedEntity>>  {

        return flow {
            emit(Result.success(apiInterface.getHomeFeed()))
        }
    }

    suspend fun saveFeedFromHereToDb(feedEntity: FeedEntity){
        localDataSource.save(feedEntity)
    }
}