package com.github.ebingelis.simplenbaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.databinding.HomePageTabBinding
import com.github.ebingelis.simplenbaapp.viewmodel.TeamsViewModel

class HomeFragment : Fragment() {

    private var _binding: HomePageTabBinding? = null

    lateinit var viewModel: TeamsViewModel
    private val teamsAdapter = TeamsListAdapter(arrayListOf())
    private lateinit var listError: TextView
    private lateinit var loading: ProgressBar
    private lateinit var teamsList: RecyclerView
    private lateinit var sortButton: AppCompatSpinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TeamsViewModel::class.java]
        viewModel.refresh()

        teamsList = binding.teamsList
        listError = binding.listError
        loading = binding.loadingView

        sortButton = binding.sortButton

        teamsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = teamsAdapter
        }

        observeViewModel()

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.order_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            sortButton.adapter = adapter
        }

        sortButton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setCurrentOrdering(resources.getStringArray(R.array.order_array)[position])
            }

        }

    }

    private fun removeObservers() {

        viewModel.teams.removeObservers(this)
        viewModel.teamsLoadError.removeObservers(this)
        viewModel.loading.removeObservers(this)

    }

    private fun observeViewModel() {

        viewModel.teams.observe(viewLifecycleOwner) { teams ->
            teams?.let { teamsAdapter.updateTeams(it) }
        }

        viewModel.teamsLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                loading.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    teamsList.visibility = View.GONE
                } else {
                    teamsList.visibility = View.VISIBLE
                }
            }
        }

    }

    private val binding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    companion object {
        fun newInstance() = HomeFragment().apply {}
    }
}