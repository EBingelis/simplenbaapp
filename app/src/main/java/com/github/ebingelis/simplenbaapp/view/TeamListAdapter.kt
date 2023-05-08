package com.github.ebingelis.simplenbaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.model.TeamsData

class TeamsListAdapter(private var teams: ArrayList<TeamsData>) :
    RecyclerView.Adapter<TeamsListAdapter.TeamsViewHolder>() {

    fun updateTeams(team: List<TeamsData>) {
        teams.clear()
        teams.addAll(team)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TeamsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.team_list_item, parent, false)
    )

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.bind(teams[position])

        val myContext = holder.itemView.context

        holder.teamItem.setOnClickListener {
            TeamBottomSheetView(myContext, holder.fullNameTextView.text.toString()).show(holder.id)
        }
    }

    class TeamsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val fullNameTextView: TextView = view.findViewById(R.id.full_name_text_view)
        private val cityTextView: TextView = view.findViewById(R.id.city_text_view)
        private val conferenceTextView: TextView = view.findViewById(R.id.conference_text_view)
        val teamItem: ConstraintLayout = view.findViewById(R.id.team_item)
        var id = 0

        fun bind(team: TeamsData) {
            fullNameTextView.text = team.fullName
            cityTextView.text = team.city
            conferenceTextView.text = team.conference
            id = team.id!!

        }

    }

}