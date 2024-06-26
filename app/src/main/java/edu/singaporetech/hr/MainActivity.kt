package edu.singaporetech.hr


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.singaporetech.hr.fragment.HomeFragment
import edu.singaporetech.hr.fragment.attendance.AttendanceOverviewFragment
import edu.singaporetech.hr.fragment.leave.LeaveFragment
import edu.singaporetech.hr.fragment.payslip.PayslipFragment


/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout:DrawerLayout

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = "HomePage"
    }
    override fun onStart() {
        super.onStart()
        supportActionBar?.title = "HomePage"
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
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                        PayslipFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

                R.id.leavePage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                        LeaveFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

                R.id.homePage->{
                    supportActionBar?.setTitle("HomePage")
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                        HomeFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

                R.id.attendencePage->{
                    supportActionBar?.setTitle("Attendance Overview")
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                        AttendanceOverviewFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

                R.id.logout->{
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, SignActivity::class.java))
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