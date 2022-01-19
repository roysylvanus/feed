package com.roysylva.feedapp



import android.os.Bundle
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

import coil.annotation.ExperimentalCoilApi
import com.roysylva.feedapp.helper.ConnectionLiveData
import com.roysylva.feedapp.ui.theme.FeedAppTheme
import com.roysylva.feedapp.ui.theme.feature.HomeFeedListUi
import com.roysylva.feedapp.ui.theme.feature.LoadingUi
import com.roysylva.feedapp.viewmodel.HomeFeedViewModel
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalCoilApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var connectivityLiveData: ConnectionLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityLiveData = ConnectionLiveData(this)

        setContent {


            FeedAppTheme {

                //internet connectivity state listener to pass boolean
                val isNetworkAvailable =  connectivityLiveData.observeAsState(false).value

                //our main operation is called
               CallApi(isNetworkAvailable = isNetworkAvailable)


            }
        }
    }
}



@ExperimentalCoilApi
@Composable
fun CallApi(isNetworkAvailable: Boolean) {

    //initiate scaffold state to be used by the snackBar
    val scaffoldState = rememberScaffoldState()

    //initiate viewModel
    val viewModel: HomeFeedViewModel = hiltViewModel()
    viewModel.load()

    //load view model data


    //collect feed as state from view model
    val allFeed by viewModel.homeFeed.collectAsState()
    val loading by viewModel.isLoading.collectAsState()

    when{
        loading-> LoadingUi(scaffoldState = scaffoldState, isNetworkAvailable = isNetworkAvailable)
        else -> HomeFeedListUi(allFeed,scaffoldState,isNetworkAvailable)
    }






}
