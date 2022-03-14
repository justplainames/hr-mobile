package edu.singaporetech.hr

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter



class HomeFragment : Fragment() {
        // TODO: Rename and change types of parameters
        private lateinit var viewModel: PayslipViewModel
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            val payslipButton= view.findViewById<Button>(R.id.payslipButton)
            val leaveButton = view.findViewById<Button>(R.id.leaveButton)

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)

            //COUNTDOWN
            viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
                val now= LocalDate.now()
                var payslipLatestDate: String =android.text.format.DateFormat.format("yyyy-MM-dd", payslip[0].dateOfPayDay).toString()

                var getDate = LocalDate.parse(payslipLatestDate.toString(), formatter)
                var currentDate = LocalDate.parse(now.toString(), formatter)
                var payday = getDate.plusDays(30)
                var periodBtw=Period.between(currentDate,payday)
                val info_text_payday_countdown = view.findViewById<TextView>(R.id.info_text_payday_countdown)
                info_text_payday_countdown.text= "${periodBtw.toString().subSequence(1,3)} DAY\n TO\n PAY DAY"
            })



            payslipButton.setOnClickListener {

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()

            }
            leaveButton.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveFragment())
                    .commitNow()
            }
            return view

        }

    }
