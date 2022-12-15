package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView

class MainActivity : AppCompatActivity() {
    //private lateinit var mMenu: Menu
    private var mMenu = R.menu.topbar_menu_search
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navControllerRailView: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView? = binding.navView
        val railView: NavigationRailView? = binding.navigationRailView
        navController = findNavController()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_favorites,
                R.id.navigation_sell
            )
        )
        //TODO Create 2 navcontroller

        navController.addOnDestinationChangedListener{  _, nd: NavDestination, _->
            if(nd.id == R.id.searchResults || nd.id == R.id.detailCarFragment || nd.id == R.id.addNewCarFragment){
                //if (navView.visibility == View.GONE) railView?.visibility = View.GONE
                //else navView.visibility = View.GONE
                navView?.visibility = View.GONE
                railView?.visibility = View.GONE
            }else{
                //if (navView.visibility == View.GONE) railView?.visibility = View.VISIBLE
                // navView.visibility = View.VISIBLE
                navView?.visibility = View.VISIBLE
                railView?.visibility = View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)
        railView?.setupWithNavController(navController)
        if (intent != null) {
            val id = intent.getLongExtra("ID", -1)
            val bundle = Bundle()
            bundle.putLong(
                "CarIdNotification", id
            )
            if (id > 0) {
                navController.navigate(R.id.detailCarFragment, bundle)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun findNavController(): NavController {
        val navHostFragment = (this as? MainActivity)?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment

        return navHostFragment!!.navController
    }
}