package com.github.ebingelis.simplenbaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.model.PlayersData

class PlayersListAdapter(var players: ArrayList<PlayersData>) :
    RecyclerView.Adapter<PlayersListAdapter.PlayersViewHolder>() {

    fun updatePlayers(newPlayers: List<PlayersData>) {

        players.clear()
        players.addAll(newPlayers)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlayersViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.team_list_item, parent, false)
    )

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.bind(players[position])

        val myContext = holder.itemView.context

        holder.teamItem.setOnClickListener {
            TeamBottomSheetView(myContext ,holder.teamNameTextView.text.toString()).show(holder.id)
        }
    }

    class PlayersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val firstNameTextView: TextView = view.findViewById(R.id.full_name_text_view)
        private val lastNameTextView: TextView = view.findViewById(R.id.city_text_view)
        val teamNameTextView: TextView = view.findViewById(R.id.conference_text_view)
        val teamItem: ConstraintLayout = view.findViewById(R.id.team_item)
        var id = 0

        fun bind(player: PlayersData) {
            firstNameTextView.text = player.firstName
            lastNameTextView.text = player.lastName
            teamNameTextView.text = player.teamFullName
            id = player.teamId!!
        }

    }

}