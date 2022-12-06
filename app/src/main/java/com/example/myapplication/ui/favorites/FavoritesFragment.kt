package com.example.myapplication.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarLogo
import com.example.myapplication.ui.detailCar.DetailCarViewModel
import com.example.myapplication.ui.detailCar.DetailViewModelFactory
import com.example.myapplication.ui.home.HomeFragmentDirections
import com.example.myapplication.ui.home.HomeListAdapter
import com.example.myapplication.utils.showCustomSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentFavoritesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        val adapter = FavoritesListAdapter(clickListener = { car ->
            val action = FavoritesFragmentDirections
                .actionNavigationFavoritesToDetailCarFragment(car.id)
            findNavController().navigate(action)
        }, functionFavorites =  { favoritesViewModel.updateFavorites(it) }, undoRemovedFavorites = {favoritesViewModel.restoreFavorite(it)}, favoritesViewModel.carLogos  )
        val observer = Observer<List<CarLogo>> {
            binding.recyclerView.adapter = adapter
        }
        favoritesViewModel.carLogos.observe(viewLifecycleOwner,observer)
        favoritesViewModel.allFavoritesCar.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it)
            }
        }

        favoritesViewModel.favoritesCarNumber.observe(this.viewLifecycleOwner) {
            binding.numberOfFavorites.text =
                getString(R.string.car_favorites_number, it.toString())
        }

        binding.apply {
            recyclerView.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}