package com.example.giantbomb.ui

import android.os.Bundle
import android.util.Log.d
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giantbomb.adapter.GamesRecyclerViewAdapter
import com.example.giantbomb.viewmodel.GamesResultsViewModel
import com.example.giantbomb.R
import com.example.giantbomb.api.GiantbombApiService
import com.example.giantbomb.data.datasource.local.InMemoryGamesStorage
import com.example.giantbomb.databinding.FragmentFirstBinding
import com.example.giantbomb.network.GamesResponse
import com.example.giantbomb.network.RetrofitClientFactory
import com.example.giantbomb.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GamesListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: GamesRecyclerViewAdapter
    private lateinit var retrofitService: GiantbombApiService
    private lateinit var searchView: SearchView
    private val viewModel: GamesResultsViewModel = GamesResultsViewModel(InMemoryGamesStorage())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        adapter = GamesRecyclerViewAdapter(requireContext(), viewModel)
        val gamesRecyclerView = binding.gamesRecyclerView
        gamesRecyclerView.adapter = adapter
        gamesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        gamesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initSearchView() {
        searchView.apply {
            queryHint = "Type here to search"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    fetchGamesBasedOnSearchText(query)
                    adapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    fetchGamesBasedOnSearchText(newText)
                    adapter.filter.filter(newText)
                    return false
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofitService = RetrofitClientFactory.createApiService(
            Constants.BASE_URL,
            GiantbombApiService::class.java
        )
        initRecyclerView()
        setObservers()
    }

    private fun setObservers() {
        viewModel.gameResults.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
            toggleGameListView(it?.isEmpty())
        })
    }

    private fun toggleGameListView(empty: Boolean?) {
        empty?.let { isEmpty ->
            if (isEmpty) {
                binding.gamesRecyclerView.visibility = View.GONE
                binding.noGamesLayout.visibility = View.VISIBLE
            } else {
                binding.gamesRecyclerView.visibility = View.VISIBLE
                binding.noGamesLayout.visibility = View.GONE
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        try {
            super.onViewStateRestored(savedInstanceState)
        } catch (e: Exception) {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        initSearchView()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun fetchGamesBasedOnSearchText(searchText: String?) {
        try {
            retrofitService.getGamesFeed(
                Constants.API_KEY,
                "name:$searchText",
                "json",
                "image,name"
            )
                .enqueue(object : Callback<GamesResponse> {
                    override fun onResponse(
                        call: Call<GamesResponse>,
                        response: Response<GamesResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            viewModel.setResults(response.body()!!.results!!)
                            adapter.onItemClick = {
                                findNavController().navigate(
                                    R.id.action_FirstFragment_to_SecondFragment,
                                    Bundle().apply {
                                        putString("label", it.name)
                                        putString("uri", it.image.original_url)
                                    })
                            }
                        } else {
                            d("GiantbombApi", "Response was null")
                        }
                    }

                    override fun onFailure(call: Call<GamesResponse>, t: Throwable) {
                        d("GiantbombApi", t.message!!)
                    }
                })
        } catch (e: IOException) {
            return
        }
    }
}