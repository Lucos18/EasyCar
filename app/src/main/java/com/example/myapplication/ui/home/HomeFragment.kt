package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels {
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.searchCars.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSearch())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}