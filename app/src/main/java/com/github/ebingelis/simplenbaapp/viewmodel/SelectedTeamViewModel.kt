package com.github.ebingelis.simplenbaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ebingelis.simplenbaapp.model.SelectedTeam
import com.github.ebingelis.simplenbaapp.model.SelectedTeamData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SelectedTeamViewModel: ViewModel() {

    val selectedTeamsData = MutableLiveData<List<SelectedTeamData>>()
    val selectedTeamsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val additionalSelectedTeamsData = MutableLiveData<List<SelectedTeamData>>()
    val additionalDataLoading = MutableLiveData<Boolean>()
    var currentPage = 1
    var maximumPages = 0


    fun refresh(id:Int){
        fetchTeamData(id)
    }

    fun nextPageData(id: Int){
        additionalDataLoading.value = true
        println(currentPage)
        println(maximumPages)
        if(currentPage < maximumPages){
            currentPage += 1
            fetchMoreTeamData(id,currentPage)
        }else{
            currentPage = 0
            fetchMoreTeamData(id,currentPage)
        }
    }

    private fun fetchMoreTeamData(id: Int, currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val selectedTeamClass = SelectedTeam()
                val data = selectedTeamClass.getTeamData(id, currentPage)

                withContext(Dispatchers.Main){
                    selectedTeamsLoadError.value = false
                    loading.value = false
                    additionalSelectedTeamsData.value = data
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    selectedTeamsLoadError.value = true
                    loading.value = false
                    selectedTeamsData.value = emptyList()
                }
            }

        }
    }

    private fun fetchTeamData(id: Int, currentPage: Int = 1){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }

                val selectedTeamClass = SelectedTeam()
                val data = selectedTeamClass.getTeamData(id, currentPage)

                withContext(Dispatchers.Main){
                    selectedTeamsLoadError.value = false
                    loading.value = false
                    selectedTeamsData.value = data
                    maximumPages = selectedTeamClass.maximumPages
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    selectedTeamsLoadError.value = true
                    loading.value = false
                    selectedTeamsData.value = emptyList()
                }
            }

        }
    }
}