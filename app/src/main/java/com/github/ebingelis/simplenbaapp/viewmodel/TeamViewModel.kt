package com.github.ebingelis.simplenbaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ebingelis.simplenbaapp.model.Teams
import com.github.ebingelis.simplenbaapp.model.TeamsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TeamViewModel: ViewModel() {

    private val currentOrdering = MutableLiveData("Name")
    val teams = MutableLiveData<List<TeamsData>>()
    val teamsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        fetchTeamsData()
    }

    fun setCurrentOrdering(sortBy: String){
        currentOrdering.value = sortBy
        sortBy()
    }

    private fun sortBy() {
        val lastData: List<TeamsData> = teams.value ?: emptyList()
        val lastDataMutableList: MutableList<TeamsData> = lastData.toMutableList()
        when (currentOrdering.value) {
            "Name" -> lastDataMutableList.sortBy { it.fullName }
            "City" -> lastDataMutableList.sortBy { it.city }
            "Conference" -> lastDataMutableList.sortBy { it.conference }
        }
        teams.value = lastDataMutableList
        println(teams.value)
    }

    private fun fetchTeamsData(){
        CoroutineScope(Dispatchers.IO).launch {

            try {

                withContext(Dispatchers.Main){
                    loading.value = true
                }


                val data = Teams().getTeamsData()

                withContext(Dispatchers.Main){
                    teamsLoadError.value = false
                    loading.value = false
                    teams.value = data
                }

            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    teamsLoadError.value = true
                    loading.value = false
                    teams.value = emptyList()
                }
            }

        }
    }
}