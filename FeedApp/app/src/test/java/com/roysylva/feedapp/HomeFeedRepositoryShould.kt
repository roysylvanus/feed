package com.roysylva.feedapp

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.roysylva.feedapp.datasource.local.FeedEntity
import com.roysylva.feedapp.datasource.local.LocalDataSource
import com.roysylva.feedapp.datasource.network.ApiInterface
import com.roysylva.feedapp.helper.Constants
import com.roysylva.feedapp.repository.HomeFeedRepository
import com.roysylva.feedapp.utils.BaseUnitTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import retrofit2.Response

class HomeFeedRepositoryShould : BaseUnitTest() {

    private val localDataSource = mock<LocalDataSource>()
    private lateinit var repository: HomeFeedRepository
    private val apiInterface :ApiInterface = mock()
    private val feedEntity = mock<FeedEntity>()
    private val expected = Result.success(feedEntity)
    private val exception = RuntimeException("Something went wrong")


    @Test
    fun getFeedFromApi() = runBlockingTest {

       repository = HomeFeedRepository(localDataSource, apiInterface)
        repository.getAllFeedFromApi().first()

        verify(apiInterface, times(1)).getHomeFeed()

    }


    @Test
    fun emitFeedFromApi() = runBlockingTest {
        mockSuccessfullCase()

        assertEquals(expected,repository.getHomeFeedResponse().first())
    }

    private suspend fun mockSuccessfullCase() {
        whenever(apiInterface.getHomeFeed()).thenReturn(feedEntity)

        repository = HomeFeedRepository(localDataSource, apiInterface)
    }

    @Test
    fun checkErrorOmission() = runBlockingTest {

        mockFailureCase()

        assertEquals(Constants.RUNTIME_EXCEPTION_MSG,repository.getHomeFeedResponse().first().exceptionOrNull()?.message)

    }

    private suspend fun mockFailureCase() {
        whenever(apiInterface.getHomeFeed()).thenThrow(exception)

        repository = HomeFeedRepository(localDataSource, apiInterface)
    }


}