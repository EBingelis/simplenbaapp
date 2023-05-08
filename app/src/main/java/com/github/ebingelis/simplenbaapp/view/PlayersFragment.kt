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

class PlayersFragment : Fragment() {

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

        playersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = playersList.layoutManager as LinearLayoutManager
                val position = layoutManager.findLastVisibleItemPosition()

                if (position == playersAdapter.itemCount - 1) {
                    if(viewModel.loading.value != true){
                        viewModel.nextPageData()
                    }
                }
            }
        })

        val searchView = binding.searchBar

        searchView.setOnClickListener {

            searchView.isIconified = false

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.toString() != "") {
                    viewModel.search(query.toString())
                    viewModel.searching.value = true
                } else {
                    viewModel.searching.value = false
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString() != "") {
                    viewModel.search(newText.toString())
                } else {
                    playersList.smoothScrollToPosition(0)
                    viewModel.refresh()
                }
                return true
            }
        })

    }

    private fun observeViewModel() {

        viewModel.players.observe(viewLifecycleOwner) { players ->
            players?.let { playersAdapter.updatePlayers(it) }
        }

        viewModel.additionalPlayersData.observe(viewLifecycleOwner) { additionalPlayersData ->
            additionalPlayersData?.let { playersAdapter.addAdditionalPlayers(it) }
        }

        viewModel.playersLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                loading.visibility = if (it) View.VISIBLE else View.GONE
                if (it && viewModel.additionalDataLoading.value == true) {
                    playersList.visibility = View.VISIBLE
                } else {
                    if (it) {
                        listError.visibility = View.GONE
                        playersList.visibility = View.GONE
                    } else {
                        playersList.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun removeObservers() {

        viewModel.players.removeObservers(this)
        viewModel.additionalPlayersData.removeObservers(this)
        viewModel.loading.removeObservers(this)
        viewModel.playersLoadError.removeObservers(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //needed to reach this fragment
    companion object {
        fun newInstance() = PlayersFragment().apply {}
    }
}