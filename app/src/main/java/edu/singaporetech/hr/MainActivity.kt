package edu.singaporetech.hr


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import edu.singaporetech.hr.PayslipViewModel


/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PayslipViewModel
    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView:NavigationView=findViewById<NavigationView>(R.id.nav_view)

        toggle=ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.payslipPage->replaceFragment(PayslipFragment(),it.title.toString())
            }
            true
        }
//        var navigatePayslip = findViewById<Button>(R.id.navigatePayslip)
//        viewModel = ViewModelProvider(this).get(PayslipViewModel::class.java)
//        navigatePayslip.setOnClickListener {
//            val intent = Intent(this, PayslipActivity::class.java)
//            startActivity(intent)
            //val payslip1= Payslip(0,400.0,100.0,200.0,3000.0,2800.0,100.0,100.0,"January 2021",2600.0,100.0,1)
           // val payslip2= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"February 2021",2600.0,0.0,1)
           //val payslip3= Payslip(0,400.0,100.0,200.0,3000.0,2800.0,100.0,100.0,"March 2021",2600.0,100.0,1)
           //val payslip4= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"April 2021",2600.0,0.0,1)
            //val payslip5= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"May 2021",2600.0,0.0,1)
            //val payslip6= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"June 2021",2600.0,0.0,1)
           //val payslip7= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"July 2021",2600.0,0.0,1)
            //val payslip8= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"August 2021",2600.0,0.0,1)
            //val payslip9= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"September 2021",2600.0,0.0,1)
            //val payslip10= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"October 2021",2600.0,0.0,1)
           //val payslip11= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"November 2021",2600.0,0.0,1)
           //val payslip12= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"December 2021",2600.0,0.0,1)
           //val payslip13= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"January 2022",2600.0,0.0,1)
            //viewModel.insert(payslip1)
            //viewModel.insert(payslip2)
            //viewModel.insert(payslip3)
            //viewModel.insert(payslip4)
            //viewModel.insert(payslip5)
            //viewModel.insert(payslip6)
            //viewModel.insert(payslip7)
            //viewModel.insert(payslip8)
            //viewModel.insert(payslip9)
            //viewModel.insert(payslip10)
           //viewModel.insert(payslip11)
            //viewModel.insert(payslip12)
           //viewModel.insert(payslip13)
        //}
    }
    private fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){

            return true
        }

        return super.onOptionsItemSelected(item)
    }

}