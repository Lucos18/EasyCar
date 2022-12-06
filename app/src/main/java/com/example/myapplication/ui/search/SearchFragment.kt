package com.example.myapplication.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.utils.carListItemsAlertDialog

class SearchFragment : Fragment() {
    val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private var result = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel.refreshDataFromNetwork()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startingPriceSearchText.doOnTextChanged { _, _, _, _ ->
            if(binding.startingPriceSearchText.text.toString().isNotEmpty()){
                result = searchViewModel.onStartingPriceChange(binding.startingPriceSearchText.text.toString().toDouble())!!
                binding.searchCarsButton.text = getString(R.string.button_result_text, result.toString())
            }
        }
        binding.endingPriceSearchText.doOnTextChanged { _, _, _, _ ->
            if(binding.endingPriceSearchText.text.toString().isNotEmpty()){
                result = searchViewModel.onEndingPriceChange(binding.endingPriceSearchText.text.toString().toDouble())!!
                binding.searchCarsButton.text = getString(R.string.button_result_text, result.toString())
            }
        }
        binding.carSearchBrandText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(),
                layoutInflater,
                searchViewModel.getDistinctBrandNames(),
                binding.carSearchBrandText,
                binding.carSearchModelText
            )
        }
        binding.carSearchBrandText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchBrandText.text.toString().isNotEmpty()){
                binding.carSearchModelText.isEnabled = true
                result = searchViewModel.onBrandChange(binding.carSearchBrandText.text.toString())!!
                binding.searchCarsButton.text = getString(R.string.button_result_text, result.toString())
            }
        }
        binding.carSearchModelText.setOnClickListener {
            carListItemsAlertDialog(
                requireContext(),
                layoutInflater,
                searchViewModel.getDistinctModelByBrand(binding.carSearchBrandText.text.toString()),
                binding.carSearchModelText,
                null
            )
        }
        binding.carSearchModelText.doOnTextChanged { _, _, _, _ ->
            if (binding.carSearchModelText.text.toString().isNotEmpty()){
                result = searchViewModel.onModelChange(binding.carSearchModelText.text.toString())!!
                binding.searchCarsButton.text = getString(R.string.button_result_text, result.toString())
            }
        }
        binding.buttonGroupVehicleState.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.newCarButton.id -> {
                        result = searchViewModel.onStateCarCheckChange(binding.newCarButton.text.toString())!!
                    }
                    binding.usedCarButton.id -> {
                        result = searchViewModel.onStateCarCheckChange(binding.usedCarButton.text.toString())!!
                    }
                }
                binding.searchCarsButton.text = getString(R.string.button_result_text, result.toString())
            }
        }
        binding.searchCarsButton.setOnClickListener {
            Log.d("ciaos", searchViewModel.allCars.value.toString())
        }
        searchViewModel.allCars.observe(this.viewLifecycleOwner) { }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}