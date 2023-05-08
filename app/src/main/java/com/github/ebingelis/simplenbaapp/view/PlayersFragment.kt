package com.github.ebingelis.simplenbaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ebingelis.simplenbaapp.databinding.SearchPlayerTabBinding
import com.github.ebingelis.simplenbaapp.viewmodel.PlayersViewModel

class PlayersFragment: Fragment() {

    private var _binding: SearchPlayerTabBinding? = null

    lateinit var viewModel: PlayersViewModel
    private val playersAdapter = PlayersListAdapter(arrayListOf())
    private lateinit var listError: TextView
    private lateinit var loading: ProgressBar
    private lateinit var playersList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchPlayerTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PlayersViewModel::class.java]
        viewModel.refresh()

        playersList = binding.playersList
        listError = binding.listError2
        loading = binding.loadingView2

        playersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playersAdapter
        }

        observeViewModel()

        val searchView = binding.searchBar

        searchView.setOnClickListener {
                // Handle click event
                searchView.isIconified = false
                //searchView.requestFocus()
            }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submit
                viewModel.search(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                viewModel.search(newText.toString())
                return true
            }
        })

    }

    private fun observeViewModel() {
        viewModel.players.observe(viewLifecycleOwner) { teams ->
            teams?.let { playersAdapter.updatePlayers(it) }
        }

        viewModel.playersLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                loading.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    playersList.visibility = View.GONE
                } else {
                    playersList.visibility = View.VISIBLE
                }
            }
        }

    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //needed to reach this fragment
    companion object {
        fun newInstance() = PlayersFragment().apply {}
    }
}