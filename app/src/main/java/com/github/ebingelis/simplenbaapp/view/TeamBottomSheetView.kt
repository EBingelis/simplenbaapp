package com.github.ebingelis.simplenbaapp.view

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.viewmodel.SelectedTeamViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class TeamBottomSheetView(private val context: Context, private val teamName: String) {

    lateinit var viewModel: SelectedTeamViewModel
    private val selectedTeamAdapter = SelectedTeamDataListAdapter(arrayListOf())
    private lateinit var listError: TextView
    private lateinit var loading: ProgressBar
    lateinit var selectedTeamList: RecyclerView
    private lateinit var homeButton: AppCompatButton
    private val activity = context as MainActivity

    fun show(id: Int) {

        val bottomSheetDialog = BottomSheetDialog(context)

        val bottomSheetView = bottomSheetDialog.layoutInflater.inflate(
            R.layout.selected_team_data,
            activity.mainView,
            false
        )

        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetDialog.setCancelable(true)

        bottomSheetDialog.setCanceledOnTouchOutside(true)

        bottomSheetDialog.show()

        selectedTeamList = bottomSheetView.findViewById(R.id.selected_team_list_view)

        viewModel = ViewModelProvider(activity)[SelectedTeamViewModel::class.java]

        viewModel.refresh(id)

        listError = bottomSheetView.findViewById(R.id.selected_team_data_list_error)

        loading = bottomSheetView.findViewById(R.id.selected_team_data_loading_view)

        homeButton = bottomSheetView.findViewById(R.id.home_button)

        val teamNameView = bottomSheetView.findViewById<TextView>(R.id.team_name_text_view)

        homeButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setOnDismissListener {
            selectedTeamList.removeAllViews()
            removeObservers()
        }

        selectedTeamList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selectedTeamAdapter
        }

        selectedTeamList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Check if the user has scrolled to the bottom of the list
                val layoutManager = selectedTeamList.layoutManager as LinearLayoutManager
                val position = layoutManager.findLastVisibleItemPosition()
                if (position == selectedTeamAdapter.itemCount - 1) {
                    viewModel.nextPageData(id)
                }
            }
        })

        teamNameView.text = teamName

        observeViewModel()

    }

    private fun removeObservers() {

        viewModel.selectedTeamsData.removeObservers(activity)
        viewModel.additionalSelectedTeamsData.removeObservers(activity)
        viewModel.selectedTeamsLoadError.removeObservers(activity)
        viewModel.loading.removeObservers(activity)

    }

    private fun observeViewModel() {
        viewModel.selectedTeamsData.observe(activity) { teamdata ->
            teamdata?.let { selectedTeamAdapter.createTeam(it) }
        }

        viewModel.additionalSelectedTeamsData.observe(activity) { teamdata ->
            teamdata?.let { selectedTeamAdapter.updateTeam(it) }
        }

        viewModel.selectedTeamsLoadError.observe(activity) { isError ->
            isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
        }

        viewModel.loading.observe(activity) { isLoading ->
            isLoading?.let {
                loading.visibility = if (it) View.VISIBLE else View.GONE
                if (it && viewModel.additionalDataLoading.value == true) {
                    selectedTeamList.visibility = View.VISIBLE
                } else {
                    if (it) {
                        listError.visibility = View.GONE
                        selectedTeamList.visibility = View.GONE
                    } else {
                        selectedTeamList.visibility = View.VISIBLE
                    }

                }

            }
        }

    }

}