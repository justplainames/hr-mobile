package edu.singaporetech.hr

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.whiteelephant.monthpicker.MonthPickerDialog
import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import edu.singaporetech.hr.databinding.FragmentPayslipConsoBinding
import java.util.*
import kotlin.properties.Delegates


class PayslipConsoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var inputpayslipDatePickerTo: String
    lateinit var inputpayslipDatePickerFrom: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipConsoBinding>(
            inflater,R.layout.fragment_payslip_conso, container, false
        )
        fun getMonthString(month: Int): String? {
            val result = when (month) {
                0 -> "Jan"
                1 -> "Feb"
                2 -> "Mar"
                3 -> "Apr"
                4 -> "May"
                5 -> "Jun"
                6 -> "Jul"
                7 -> "Aug"
                8 -> "Sept"
                9 -> "Oct"
                10 -> "Nov"
                11 -> "Dec"

                else -> null
            }
            return result
        }
        fun getMonthInt(month: String): Int? {
            val result = when (month) {
                "Jan" -> 0
                "Feb" -> 1
                "Mar" ->2
                "Apr" ->3
                "May" ->4
                "Jun" ->5
                "Jul" ->6
                "Aug" ->7
                "Sept"->8
                "Oct"->9
                "Nov" ->10
                "Dec" -> 11

                else -> null
            }
            return result
        }
        val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val dateTobuilder = MonthPickerDialog.Builder(
            this@PayslipConsoFragment.requireActivity(), { selectedMonthTo, selectedYearTo ->
                inputpayslipDatePickerTo = "${getMonthString(selectedMonthTo)} $selectedYearTo"

                binding.payslipDatePickerToInput.setText(inputpayslipDatePickerTo)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)

        )

        dateTobuilder.setActivatedMonth(today.get(Calendar.MONTH))
            .setMinYear(2022)
            .setActivatedYear(2022)
            .setMaxYear(2022)
            .setTitle("SELECT MONTH AND YEAR OF PAYSLIP")
            .setMinMonth(Calendar.FEBRUARY)
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

            binding.payslipDatePickerTo.setEndIconOnClickListener{
                dateTobuilder.build().show()
            }
        val dateFrombuilder = MonthPickerDialog.Builder(
            this@PayslipConsoFragment.requireActivity(), { selectedMonthFrom, selectedYearFrom ->
                inputpayslipDatePickerFrom = "${getMonthString(selectedMonthFrom)} $selectedYearFrom"

                binding.payslipDatePickerFromInput.setText(inputpayslipDatePickerFrom)
            },

            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)

        )

        dateFrombuilder.setActivatedMonth(today.get(Calendar.MONTH))
            .setMinYear(2022)
            .setActivatedYear(2022)
            .setMaxYear(2022)
            .setTitle("SELECT MONTH AND YEAR OF PAYSLIP")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

        binding.payslipDatePickerFrom.setEndIconOnClickListener{
            dateFrombuilder.build().show()
        }

        binding.submitButton.setOnClickListener {
            if (binding.payslipDatePickerToInput.text.isNullOrBlank() || binding.payslipDatePickerFromInput.text.isNullOrBlank()) {
                Toast.makeText(
                    this@PayslipConsoFragment.requireActivity(),
                    "Please fill in all fields before submit",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }
}