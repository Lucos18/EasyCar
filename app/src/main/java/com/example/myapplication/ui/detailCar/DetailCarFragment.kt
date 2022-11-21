package com.example.myapplication.ui.detailCar

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.model.Car
import com.example.myapplication.ui.favorites.FavoritesViewModel
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.HomeViewModelFactory
import com.example.myapplication.ui.sell.SellFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*

class DetailCarFragment : Fragment() {
    private val detailCarArgs: DetailCarFragmentArgs by navArgs()
    private lateinit var car: Car
    private val detailCarViewModel: DetailCarViewModel by viewModels {
        DetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentDetailCarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView = requireActivity().findViewById(com.example.myapplication.R.id.nav_view)
        navBar.visibility = View.GONE
        val id = detailCarArgs.carId
        if(id > 0)
        {
            detailCarViewModel.getCarById(id).observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
        }
        if (detailCarArgs.isUserCreated)
        {
            binding.deleteCarFab.visibility = View.VISIBLE
            binding.deleteCarFab.setOnClickListener{
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Delete Car")
                //set message for alert dialog
                builder.setMessage("Are you sure do you want to delete this car from database?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    detailCarViewModel.deleteCarById(detailCarArgs.carId)
                    val action = DetailCarFragmentDirections
                        .actionDetailCarFragmentToNavigationSell()
                    findNavController().navigate(action)
                }

                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->

                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    override fun onDestroyView() {
        val navBar: BottomNavigationView = requireActivity().findViewById(com.example.myapplication.R.id.nav_view)
        navBar.visibility = View.VISIBLE
        super.onDestroyView()
        _binding = null
    }
    fun bindCar(car: Car){

        binding.apply {
            carBrandDetail.text = car.brand
            carModelDetail.text = car.model
            carPriceDetail.text = priceToCurrency(car.price)
            carPowerDetail.text = getString(R.string.car_power_detail_string, car.carPower.toString())
            carFuelTypeDetail.text = getString(R.string.car_fuel_type_detail_string, car.fuelType)
            carSeatsTypeDetail.text = getString(R.string.car_seats_detail_string, car.seats.toString())
            carYearProductionDetail.text = getString(R.string.car_year_detail_string, car.yearStartProduction.toString())
            carNotesDetailLabel.text = getString(R.string.car_notes_detail_string_label)
        }
    }
    //TODO add method to convert price to currency
    fun priceToCurrency(price: Double): String{
        val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(car.price)
    }
}