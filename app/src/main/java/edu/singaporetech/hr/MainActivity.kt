package edu.singaporetech.hr


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import edu.singaporetech.hr.PayslipViewModel
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import edu.singaporetech.hr.LeaveFragment


/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var viewModel: PayslipViewModel
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout:DrawerLayout

    override fun onResume() {
        super.onResume()
        supportActionBar?.setTitle("HomePage")

    }
    override fun onStart() {
        super.onStart()
        supportActionBar?.setTitle("HomePage")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        supportActionBar?.setTitle("HomePage")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView:NavigationView=findViewById<NavigationView>(R.id.nav_view)

        toggle=ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        navView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.payslipPage->{

                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,PayslipFragment()).commit()
                    drawerLayout.closeDrawers()
                    //setActionBarTitle(it.title.toString())

                }

                R.id.leavePage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                        LeaveFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

                R.id.homePage->{
                    supportActionBar?.setTitle("HomePage")
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()
                    drawerLayout.closeDrawers()
                    //setActionBarTitle(it.title.toString())
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){

            return true
        }

        return super.onOptionsItemSelected(item)
    }


}