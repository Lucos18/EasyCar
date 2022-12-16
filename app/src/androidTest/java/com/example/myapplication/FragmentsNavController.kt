package com.example.myapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.ui.addCar.AddNewCarFragment
import com.example.myapplication.ui.favorites.FavoritesFragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.search.SearchFragment
import com.example.myapplication.ui.sell.SellFragment
import org.mockito.Mockito

val mockNavController = Mockito.mock(NavController::class.java)
val themeApplication = R.style.Theme_MyApplication
fun startAddNewCarFragment():NavController{
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
fun startSellFragment():NavController{
    launchFragmentInContainer<SellFragment>(themeResId = themeApplication) {
        SellFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}
fun startFavoritesFragment():NavController{
    launchFragmentInContainer<FavoritesFragment>(themeResId = themeApplication) {
        FavoritesFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}
fun startSearchFragment():NavController{
    launchFragmentInContainer<SearchFragment>(themeResId = themeApplication) {
        SearchFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}
fun startHomeFragment():NavController{
    launchFragmentInContainer<HomeFragment>(themeResId = themeApplication) {
        HomeFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}