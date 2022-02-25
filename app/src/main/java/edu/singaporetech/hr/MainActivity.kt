package edu.singaporetech.hr


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import edu.singaporetech.hr.PayslipViewModel
import edu.singaporetech.hr.databinding.ActivityMainBinding


/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {

    private lateinit var homepageDrawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        homepageDrawerLayout = binding.homepageDrawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment_main)

        NavigationUI.setupActionBarWithNavController(this,navController, homepageDrawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.toPayslip -> {
                    val intent = Intent(this, PayslipActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                }
                R.id.toAttendance -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true

                }
                else -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                }

            }
            NavigationUI.onNavDestinationSelected(it, navController)
            homepageDrawerLayout.closeDrawer(GravityCompat.START)
            true


        }


    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment_main)
        return NavigationUI.navigateUp(navController, homepageDrawerLayout)
    }

}