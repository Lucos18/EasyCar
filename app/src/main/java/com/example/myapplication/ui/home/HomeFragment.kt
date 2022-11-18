package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import java.lang.Exception

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
        homeViewModel.addCar("Audi", "8000", 2010, 2022,5,"Electric", 1000.00)
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