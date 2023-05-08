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
    var currentPage = 1
    var maximumPages = 0

    fun refresh(){
        fetchPlayersData()
    }

    fun nextPageData(id: Int){
        additionalDataLoading.value = true
        println(currentPage)
        println(maximumPages)
        if(currentPage < maximumPages){
            currentPage += 1
            fetchMorePlayersData(currentPage = currentPage)
        }else{
            currentPage = 0
            fetchMorePlayersData(currentPage = currentPage)
        }
    }

    fun search(string:String){
        fetchPlayersData(string)
    }

    private fun fetchMorePlayersData(string: String = "", currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val playersClass = Players()
                val data = playersClass.getPlayersData(string, )

                withContext(Dispatchers.Main){
                    playersLoadError.value = false
                    loading.value = false
                    players.value = data
                    maximumPages = playersClass.maximumPages
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

    private fun fetchPlayersData(string: String = "", currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val data = Players().getPlayersData(string)

                withContext(Dispatchers.Main){
                    playersLoadError.value = false
                    loading.value = false
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