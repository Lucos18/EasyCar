package com.example.myapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.ui.addCar.AddNewCarFragment
import org.mockito.Mockito

fun startAddNewCarFragment():NavController{
    val mockNavController = Mockito.mock(NavController::class.java)
    launchFragmentInContainer<AddNewCarFragment>(themeResId = R.style.Theme_MyApplication) {
        AddNewCarFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}