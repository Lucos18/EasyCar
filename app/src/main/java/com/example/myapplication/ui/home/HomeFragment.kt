package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.model.CarLogo
import com.example.myapplication.utils.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        val adapter = HomeListAdapter(clickListener = { car ->
            val action = HomeFragmentDirections
                .actionNavigationHomeToDetailCarFragment(car.id)
            findNavController().navigate(action)
        }, functionFavorites = { homeViewModel.updateFavorites(it) }, homeViewModel.carLogos)
        val observer = Observer<List<CarLogo>> {
            binding.recyclerView.adapter = adapter
        }
        homeViewModel.carLogos.observe(viewLifecycleOwner,observer)
        homeViewModel.allCars.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it.toMutableList())
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
        }

        binding.searchCars.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSearch())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}