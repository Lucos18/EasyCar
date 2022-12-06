package com.example.myapplication.ui.searchResults

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.databinding.FragmentSearchResultsBinding
import com.example.myapplication.model.CarLogo
import com.example.myapplication.ui.home.HomeFragmentDirections
import com.example.myapplication.ui.home.HomeListAdapter
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.HomeViewModelFactory
import com.example.myapplication.ui.search.SearchViewModel
import com.example.myapplication.ui.search.SearchViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchResults : Fragment() {
    //TODO Fix initial result value on search button when going back
    //TODO Fix SearchResultFragment item card favorites not working (Observe missing)
    //TODO Fix Model car on item that goes over the card
    //TODO Fix BottomNavigation that needs to be disabled when going to Search Results
    //TODO Fix Chip from material theme not working (Will crash the app)
    //TODO Add More Filters to the application (Vehicle Fuel)
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentSearchResultsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        val adapter = HomeListAdapter(clickListener = { car ->
            val action = SearchResultsDirections
                .actionSearchResultsToDetailCarFragment(car.id)
            findNavController().navigate(action)
        }, functionFavorites = { homeViewModel.updateFavorites(it) }, homeViewModel.carLogos)
        val observer = Observer<List<CarLogo>> {
            binding.recyclerView.adapter = adapter
        }
        homeViewModel.carLogos.observe(viewLifecycleOwner,observer)
        adapter.submitList(searchViewModel.filteredList)
        binding.apply {
            recyclerView.adapter = adapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}