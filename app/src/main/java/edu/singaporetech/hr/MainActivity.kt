package edu.singaporetech.hr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.firstapp.Payslip
import edu.singaporetech.firstapp.PayslipViewModel


/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:PayslipViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navigatePayslip=findViewById<Button>(R.id.navigatePayslip)
        viewModel= ViewModelProvider(this).get(PayslipViewModel::class.java)
        navigatePayslip.setOnClickListener{
            val intent = Intent(this, PayslipActivity::class.java)
            startActivity(intent)
//            val payslip2= Payslip(0,400.0,200.0,200.0,2900.0,3000.0,0.0,0.0,"August 2021",2600.0,100.0,1)
//            viewModel.insert(payslip2)
        }
    }
}