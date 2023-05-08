package com.github.ebingelis.simplenbaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ebingelis.simplenbaapp.model.Players
import com.github.ebingelis.simplenbaapp.model.PlayersData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class PlayersViewModel: ViewModel() {

    val players = MutableLiveData<List<PlayersData>>()
    val playersLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val additionalPlayersData = MutableLiveData<List<PlayersData>>()
    val additionalDataLoading = MutableLiveData<Boolean>()
    val searching = MutableLiveData(false)
    private var searchString = ""
    private var currentPage = 1
    private var maximumPages = 0

    fun refresh(){
        searchString = ""
        fetchPlayersData()
    }

    fun nextPageData(){
        additionalDataLoading.value = true
        println(currentPage)
        println(maximumPages)

        if(currentPage == maximumPages){
            if(searchString == ""){
                currentPage = 0
                fetchMorePlayersData(currentPage = currentPage)
            }
        }else{

            currentPage += 1
            if(searchString == ""){
                fetchMorePlayersData(currentPage = currentPage)
            }else{
                fetchMorePlayersData(searchString, currentPage = currentPage)
            }
        }

    }

    fun search(string:String){
        searchString = string
        currentPage = 1
        fetchPlayersData(string)
    }

    private fun fetchMorePlayersData(string: String = "", currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val playersClass = Players()
                val data = playersClass.getPlayersData(string, currentPage)

                withContext(Dispatchers.Main){
                    playersLoadError.value = false
                    loading.value = false
                    maximumPages = playersClass.maximumPages
                    additionalDataLoading.value = false
                    additionalPlayersData.value = data
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    playersLoadError.value = true
                    loading.value = false
                    additionalPlayersData.value = emptyList()
                }
            }

        }
    }
    private fun fetchPlayersData(string: String = "", currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val playersClass = Players()
                val data = playersClass.getPlayersData(string, currentPage)

                withContext(Dispatchers.Main){
                    playersLoadError.value = false
                    loading.value = false
                    maximumPages = playersClass.maximumPages
                    players.value = data
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    playersLoadError.value = true
                    loading.value = false
                    players.value = emptyList()
                }
            }

        }
    }

}