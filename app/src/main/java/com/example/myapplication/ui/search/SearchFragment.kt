package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.BaseApplication
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.HomeViewModelFactory

class SearchFragment : Fragment() {
    val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}