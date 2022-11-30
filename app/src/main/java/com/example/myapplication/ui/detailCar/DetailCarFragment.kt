package com.example.myapplication.ui.detailCar

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailCarBinding
import com.example.myapplication.model.*
import com.example.myapplication.ui.transformIntoDatePicker
import com.example.myapplication.utils.FuelTypeAlertDialog
import com.example.myapplication.utils.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.example.myapplication.utils.showCustomSnackBar
import com.example.myapplication.workers.CarWorkerViewModel
import com.example.myapplication.workers.CarWorkerViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import java.lang.System.load
import java.util.*
import java.util.concurrent.TimeUnit

class DetailCarFragment : Fragment() {
    var oldCarMileage: Double = 0.0
    private val detailCarArgs: DetailCarFragmentArgs by navArgs()
    private lateinit var car: Car
    private var carLogo = MutableLiveData<List<CarLogo>>()
    private val detailCarViewModel: DetailCarViewModel by viewModels {
        DetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }
    private val viewModel: CarWorkerViewModel by viewModels {
        CarWorkerViewModelFactory(requireActivity().application)
    }
    private var _binding: FragmentDetailCarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailCarViewModel.refreshDataFromNetwork()
        _binding = FragmentDetailCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getLong("CarIdNotification")
        setupViewSwitcher()
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        var id = detailCarArgs.carId
        if (arg != null && arg > 0)
            id = arg
        if (id > 0) {
            detailCarViewModel.getCarById(id).observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
        }
        if (detailCarArgs.isUserCreated) {
            binding.deleteCarFab.visibility = View.VISIBLE
            binding.editCarFab.visibility = View.VISIBLE
            binding.deleteCarFab.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle(getString(R.string.delete_car_dialog_title))
                //set message for alert dialog
                builder.setMessage(getString(R.string.delete_car_dialog_message))
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton(getString(R.string.delete_car_dialog_positive_button)) { _, _ ->
                    detailCarViewModel.deleteCarById(detailCarArgs.carId)
                    val action = DetailCarFragmentDirections
                        .actionDetailCarFragmentToNavigationSell()
                    findNavController().navigate(action)
                }

                //performing negative action
                builder.setNegativeButton(getString(R.string.delete_car_dialog_negative_button)) { _, _ ->

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
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.VISIBLE
        super.onDestroyView()
        _binding = null
    }

    private fun bindCar(car: Car) {
        binding.apply {
            binding.carYearProductionEditText.transformIntoDatePicker(
                requireContext(),
                "yyyy",
                Date()
            )
            binding.carFuelTypeEditText.setOnClickListener {
                FuelTypeAlertDialog(requireContext(), binding.carFuelTypeEditText)
            }
            editCarFab.setOnClickListener {
                oldCarMileage = car.mileage
                switchBetweenEditAndSave()
                setEditTextBinding()
                deleteCarFab.visibility = View.GONE
                editCarFab.visibility = View.GONE
                saveCarFab.visibility = View.VISIBLE
            }
            saveCarFab.setOnClickListener {
                if (checkInput()) {
                    switchBetweenEditAndSave()
                    deleteCarFab.visibility = View.VISIBLE
                    editCarFab.visibility = View.VISIBLE
                    saveCarFab.visibility = View.GONE
                    editCarFab.setImageResource(R.drawable.ic_baseline_edit_24)
                    detailCarViewModel.updateCar(
                        car,
                        power = carPowerEditText.text.toString(),
                        seats = carSeatsEditText.text.toString(),
                        fuelType = carFuelTypeEditText.text.toString(),
                        year = carYearProductionEditText.text.toString(),
                        price = carPriceEditText.text.toString(),
                        mileage = carMileageTextEdit.text.toString()
                    )
                    val checkMileage =
                        binding.carMileageTextEdit.text.toString().toDouble() - oldCarMileage
                    if (checkMileage >= 100000) viewModel.scheduleReminder(
                        5,
                        TimeUnit.SECONDS,
                        getString(
                            R.string.service_car_expired_text, car.brand
                        ),
                        getString(R.string.service_car_context_text, car.brand, car.model),
                        car.id,
                        car.brand,
                        car.model,
                        car.image
                    )
                } else {
                    showCustomSnackBar(
                        binding.coordinatorDetailCar,
                        getString(R.string.error_validation_edit),
                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    )
                }
            }
            carBrandDetail.text = car.brand
            carModelDetail.text = car.model
            carPriceDetail.text = car.formatPriceToCurrency(car.price)
            carPowerDetail.text = car.carPowerWithUnitString(car.carPower)
            carMileageText.text = car.carMileageWithUnitString(car.mileage)
            carFuelTypeDetail.text = getString(R.string.car_fuel_type_detail_string, car.fuelType)
            carSeatsTypeDetail.text =
                getString(R.string.car_seats_detail_string, car.seats.toString())
            carYearProductionDetail.text =
                getString(R.string.car_year_detail_string, car.yearStartProduction.toString())
            carStateText.text =
                if (car.mileage > 0) getString(R.string.used_text) else getString(R.string.new_text)

            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.carImageDetail.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        bmp,
                        1920,
                        1080,
                        false
                    )
                )
            } else {
                binding.carImageDetail.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }
            val observer = Observer<List<CarLogo>> { list ->
                setAndGetUriByBrandParsingListOfLogoAndImageView(
                    list,
                    car.brand,
                    binding.carLogoDetail
                )
            }
            detailCarViewModel.carLogos.observe(viewLifecycleOwner,observer)
        }
    }

    private fun setupViewSwitcher() {
        val inAnim = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        binding.apply {
            yearProductionSwitcher.inAnimation = inAnim
            fuelTypeSwitcher.inAnimation = inAnim
            seatsSwitcher.inAnimation = inAnim
            powerSwitcher.inAnimation = inAnim
            mileageSwitcher.inAnimation = inAnim
        }


        val out = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
        binding.apply {
            yearProductionSwitcher.outAnimation = out
            fuelTypeSwitcher.outAnimation = out
            seatsSwitcher.outAnimation = out
            powerSwitcher.outAnimation = out
            mileageSwitcher.outAnimation = out
        }

    }

    fun setEditTextBinding() {
        binding.apply {
            carYearProductionEditText.setText(car.yearStartProduction.toString())
            carPowerEditText.setText(car.carPower.toString())
            carSeatsEditText.setText(car.seats.toString())
            carFuelTypeEditText.setText(car.fuelType)
            carPriceEditText.setText(car.price.toString())
            carMileageTextEdit.setText(car.mileage.toString())
        }
    }

    fun switchBetweenEditAndSave() {
        binding.apply {
            powerSwitcher.showNext()
            seatsSwitcher.showNext()
            yearProductionSwitcher.showNext()
            fuelTypeSwitcher.showNext()
            priceSwitcher.showNext()
            mileageSwitcher.showNext()
        }
    }

    fun checkInput(): Boolean {
        return detailCarViewModel.checkInputEditTextNewCar(
            power = binding.carPowerEditText.text.toString().toInt(),
            seats = binding.carSeatsEditText.text.toString().toInt(),
            fuelType = binding.carFuelTypeEditText.text.toString(),
            year = binding.carYearProductionEditText.text.toString().toInt(),
            price = binding.carPriceEditText.text.toString().toDouble(),
            mileage = binding.carMileageTextEdit.text.toString().toDouble()
        )
    }
}