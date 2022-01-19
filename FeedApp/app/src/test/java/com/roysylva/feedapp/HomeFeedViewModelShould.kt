package com.roysylva.feedapp

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.asLiveData
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.roysylva.feedapp.datasource.local.FeedEntity
import com.roysylva.feedapp.model.Card
import com.roysylva.feedapp.repository.HomeFeedRepository
import com.roysylva.feedapp.utils.BaseUnitTest
import com.roysylva.feedapp.viewmodel.HomeFeedViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


class HomeFeedViewModelShould : BaseUnitTest(){

    private val homeFeedRepository:HomeFeedRepository = mock()
    private val homeFeed = mock<FeedEntity>()
    private val homeFeedList = mock<List<Card>>()
    private val listExpectation = Result.success(homeFeedList)
    private val expected = Result.success(homeFeed)
    private val exception = RuntimeException("Something went wrong")


    //test to get list from repository
    @Test
    fun getListFromRepository() = runBlockingTest {

        val viewModel = mockSuccessfulCase()
        viewModel.load()

        verify(homeFeedRepository, times(1)).getHomeFeedResponse()

    }



    private suspend fun mockSuccessfulCase(): HomeFeedViewModel {
        whenever(homeFeedRepository.getHomeFeedResponse()).thenReturn(flow {
            emit(expected)
        })
        val viewModel = HomeFeedViewModel(homeFeedRepository)
        return viewModel
    }






}