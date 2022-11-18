package com.example.myapplication.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {

            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root

        } catch (e: Exception) {
            Log.e("ciao", "onCreateView", e);
            throw e;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.addCar("Aaaaaaaaaaaa", "88000", 1910, 2022,5,"Electric", 10000.00)
        val adapter = HomeListAdapter { car ->
            val action = HomeFragmentDirections
                .actionNavigationHomeToDetailCarFragment(car.id)
            findNavController().navigate(action)
        }
        homeViewModel.allCars.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            recyclerView.adapter = adapter
        }

        binding.searchCars.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSearch())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}