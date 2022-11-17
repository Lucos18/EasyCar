package com.example.myapplication.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
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
            val root: View = binding.root

            val textView: TextView = binding.textHome
            homeViewModel.text.observe(viewLifecycleOwner) {
                textView.text = it
            }
            return root
        }catch (e: Exception) {
            Log.e("ciao", "onCreateView", e);
            throw e;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        val adapter = HomeListAdapter { car ->
            val action = CarListFragmentDirections
                .actionCarListFragmentToCarDetailsFragment(car.id)
            findNavController().navigate(action)
        }
        homeViewModel.allCars.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            carRecyclerViewList.adapter = adapter
        }
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}