package com.github.ebingelis.simplenbaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.model.SelectedTeamData

class SelectedTeamDataListAdapter(var teams: ArrayList<SelectedTeamData>) :
    RecyclerView.Adapter<SelectedTeamDataListAdapter.SelectedTeamDataViewHolder>() {

    fun updateTeam(team: List<SelectedTeamData>) {
        teams.addAll(team)
        notifyDataSetChanged()
    }

    fun createTeam(team: List<SelectedTeamData>){
        teams.clear()
        teams.addAll(team)
        notifyDataSetChanged()
    }

    fun deleteTeam(){
        teams.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SelectedTeamDataViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.selected_team_data_item, parent, false)


    )

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: SelectedTeamDataViewHolder, position: Int) {
        holder.bind(teams[position])

    }

    class SelectedTeamDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val homeTeamFullNameView: TextView = view.findViewById(R.id.home_team_full_name_text_view)
        private val homeTeamScoreView: TextView = view.findViewById(R.id.home_team_score_text_view)
        private val visitorTeamFullNameView: TextView = view.findViewById(R.id.visitor_team_full_name_text_view)
        private val visitorTeamScoreView: TextView = view.findViewById(R.id.visitor_team_score_text_view)

        fun bind(selectedTeam: SelectedTeamData) {
            homeTeamFullNameView.text = selectedTeam.homeTeamFullName
            homeTeamScoreView.text = selectedTeam.homeTeamScore
            visitorTeamFullNameView.text = selectedTeam.visitorTeamFullName
            visitorTeamScoreView.text = selectedTeam.visitorTeamScore
        }

    }

}