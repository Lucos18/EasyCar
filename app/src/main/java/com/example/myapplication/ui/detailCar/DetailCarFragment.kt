package com.example.myapplication.ui.detailCar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.model.Car
import com.example.myapplication.ui.favorites.FavoritesViewModel
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.HomeViewModelFactory

class DetailCarFragment : Fragment() {
    private val detailCarArgs: DetailCarFragmentArgs by navArgs()
    private lateinit var car: Car
    val detailCarViewModel: DetailCarViewModel by activityViewModels {
        DetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentDetailCarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)

        _binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = detailCarArgs.carId
        if(id > 0)
        {
            detailCarViewModel.getCarById(id).observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun bindCar(car: Car){
        binding.apply {
            carBrandDetail.text = car.brand
            carModelDetail.text = car.model
            carPriceDetail.text = car.price.toString()
            carPowerDetail.text = "8kw da cambiare"
            carFuelType.text = car.fuelType
            carYearProductionDetail.text = car.yearStartProduction.toString()
        }
    }
}