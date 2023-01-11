package com.example.myapplication.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.enums.CarFiltersFavourites
import com.example.myapplication.model.Car
import com.example.myapplication.model.CarLogo
import com.example.myapplication.utils.FavouritesFilterAlertDialog
import com.example.myapplication.utils.checkedItemFavourites

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
        setHasOptionsMenu(true)
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavoritesListAdapter(
            clickListener = { car ->
                val action = FavoritesFragmentDirections
                    .actionNavigationFavoritesToDetailCarFragment(car.id)
                findNavController().navigate(action)
            },
            functionFavorites = { favoritesViewModel.updateFavorites(it) },
            undoRemovedFavorites = { favoritesViewModel.restoreFavorite(it) },
            favoritesViewModel.carLogos
        )
        val observer = Observer<List<CarLogo>> {
            binding.recyclerView.adapter = adapter
        }
        favoritesViewModel.carLogos.observe(viewLifecycleOwner, observer)
        favoritesViewModel.allFavoritesCar.observe(this.viewLifecycleOwner) { carSelected ->
            //favoritesViewModel.allFavoritesCarSortedByFilter = favoritesViewModel.allFavoritesCar.value
                carSelected.let {
                    adapter.submitList(it)
                }
        }
        favoritesViewModel.allFavoritesCarSortedByFilter.observe(this.viewLifecycleOwner) { carSelected ->
            Log.d("ciao", favoritesViewModel.allFavoritesCarSortedByFilter.value.toString())
            if (checkedItemFavourites != -1 && favoritesViewModel.allFavoritesCarSortedByFilter.value != null) {
                carSelected.let {
                    adapter.submitList(it)
                }
            }
        }

        favoritesViewModel.favoritesCarNumber.observe(this.viewLifecycleOwner) {
            binding.numberOfFavorites.text =
                getString(R.string.car_favorites_number, it.toString())
            if (it.equals(0)) {
                binding.recyclerView.visibility = View.GONE
                binding.constraintLayoutNoFavorites.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.constraintLayoutNoFavorites.visibility = View.GONE
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
        }

        binding.noFavouritesButton.setOnClickListener {
            val action = FavoritesFragmentDirections
                .actionNavigationFavoritesToNavigationHome()
            findNavController().navigate(action)
        }
    }
    fun applyFiltersFavorites(){
        CarFiltersFavourites.values().forEach { filter ->
            if (checkedItemFavourites != -1){
                if (filter.isSelected){
                    when(filter){
                        CarFiltersFavourites.MILEAGE_ASCENDING_ORDER -> favoritesViewModel.sortByMileageAscendingOrder()
                        //TODO add here other
                        else -> null
                    }
                }
                //loadNewList(favoritesViewModel.allFavoritesCarSortedByFilter)
            } else {
                //TODO add here something to return the original list of favorites
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbar_menu_favorites, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                FavouritesFilterAlertDialog(requireContext(), ::applyFiltersFavorites)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}