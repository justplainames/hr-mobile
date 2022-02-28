package edu.singaporetech.hr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {
        // TODO: Rename and change types of parameters
        //private lateinit var viewModel: PayslipViewModel
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            val payslipButton= view.findViewById<Button>(R.id.payslipButton)
                //viewModel = ViewModelProvider(this).get(PayslipViewModel::class.java)
            payslipButton.setOnClickListener {
//                val payslip1= Payslip(0,400.0,100.0,200.0,3000.0,2800.0,100.0,100.0,"January 2021",2600.0,100.0,1)
//                 val payslip2= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"February 2021",2600.0,0.0,1)
//                val payslip3= Payslip(0,400.0,100.0,200.0,3000.0,2800.0,100.0,100.0,"March 2021",2600.0,100.0,1)
//                val payslip4= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"April 2021",2600.0,0.0,1)
//                val payslip5= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"May 2021",2600.0,0.0,1)
//                val payslip6= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"June 2021",2600.0,0.0,1)
//                val payslip7= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"July 2021",2600.0,0.0,1)
//                val payslip8= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"August 2021",2600.0,0.0,1)
//                val payslip9= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"September 2021",2600.0,0.0,1)
//                val payslip10= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"October 2021",2600.0,0.0,1)
//                val payslip11= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"November 2021",2600.0,0.0,1)
//                val payslip12= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"December 2021",2600.0,0.0,1)
//                val payslip13= Payslip(0,300.0,100.0,100.0,2900.0,2800.0,100.0,100.0,"January 2022",2600.0,0.0,1)
//
//                viewModel.insert(payslip1)
//                viewModel.insert(payslip2)
//                viewModel.insert(payslip3)
//                viewModel.insert(payslip4)
//                viewModel.insert(payslip5)
//                viewModel.insert(payslip6)
//                viewModel.insert(payslip7)
//                viewModel.insert(payslip8)
//                viewModel.insert(payslip9)
//                viewModel.insert(payslip10)
//                viewModel.insert(payslip11)
//                viewModel.insert(payslip12)
//                viewModel.insert(payslip13)
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()

            }
            return view

        }

    }