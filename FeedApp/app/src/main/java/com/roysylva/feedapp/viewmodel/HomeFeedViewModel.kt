package com.roysylva.feedapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.roysylva.feedapp.model.Card
import com.roysylva.feedapp.repository.HomeFeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel
@Inject constructor(private val homeFeedRepository: HomeFeedRepository) : ViewModel() {


    private val _isLoading = MutableStateFlow(true)
    val isLoading :StateFlow<Boolean>  = _isLoading
    private val _homeFeed = MutableStateFlow(emptyList<Card>())
    val homeFeed: StateFlow<List<Card>> get() = _homeFeed


    fun load(){
        viewModelScope.launch(Dispatchers.Default){
         homeFeedRepository.getHomeFeedResponse().onEach {
             _isLoading.value = true
         }.collectLatest {
             _homeFeed.value = it.getOrNull()?.page!!.cards
             _isLoading.value = false
             Log.e("FEEd",it.toString())
         }
        }

    }


}


