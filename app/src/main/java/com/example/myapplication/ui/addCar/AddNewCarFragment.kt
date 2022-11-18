package com.example.myapplication.ui.addCar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewCarBinding
import com.example.myapplication.databinding.FragmentHomeBinding

class AddNewCarFragment : Fragment() {

    val addNewCarViewModel: AddNewCarViewModel by activityViewModels {
        AddNewCarViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentAddNewCarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //TODO change when the button will be invisible
            buttonAddNewCar.visibility = View.VISIBLE
            buttonAddNewCar.setOnClickListener{
                addNewCar()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    fun addNewCar(){
        if(isValidCar()){
            val toast = Toast.makeText(requireContext(), "macchina valida", Toast.LENGTH_LONG)
            toast.show()
        } else {
            val toast = Toast.makeText(requireContext(), "macchina non valida", Toast.LENGTH_LONG)
            toast.show()
        }
    }
    fun isValidCar():Boolean{
        return addNewCarViewModel.checkInputEditTextNewCar(
                binding.carBrandAddText.text.toString()
        )
    }

}