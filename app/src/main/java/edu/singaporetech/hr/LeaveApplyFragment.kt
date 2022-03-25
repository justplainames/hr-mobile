package edu.singaporetech.hr

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.hr.databinding.FragmentLeaveApplyBinding

class LeaveApplyFragment : Fragment() {

    private lateinit var binding: FragmentLeaveApplyBinding
//    private val viewModel = ViewModelProvider(this).get(LeaveViewModel::class.java)
    private val viewModel: LeaveViewModel by viewModels()
    private var leave = Leave()
    var selectedDay:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leave_apply,container, false
        )


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveFragment())
                    .commitNow()
            }
        })

        // Spinner for Leave Type
        val dropDownLeaveType = resources.getStringArray(R.array.leaveType)
        val leaveTypeArrayAdaptor =
            ArrayAdapter(requireContext(), R.layout.leavetype_dropdown_item, dropDownLeaveType)
        binding.autoCompleteTextViewLeaveType.setAdapter(leaveTypeArrayAdaptor)


        // Date Picker for Start Date
        binding.textViewLeaveStartDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    binding.textViewLeaveStartDate.text = date
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        // Date Picker for End Date
        binding.textViewLeaveEndDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    binding.textViewLeaveEndDate.text = date
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }


        // Radio Group for Leave Day
        binding.radioGroupLeaveDay.setOnCheckedChangeListener { group, checkedId ->

            if (binding.radioButtonFullDay.id == checkedId) {
                selectedDay = "Full Day"
            } else if (binding.radioButtonHalfDay.id == checkedId){
                selectedDay = "Half Day"
            } else {
                selectedDay = "0"
            }
        }

        // Spinner for Supervisor
        val dropDownLeaveSupervisor = resources.getStringArray(R.array.leaveSupervisor)
        val leaveSupervisorArrayAdaptor = ArrayAdapter(
            requireContext(),
            R.layout.leave_supervisor_dropdown_item,
            dropDownLeaveSupervisor
        )
        binding.autoCompleteTextViewLeaveSupervisor.setAdapter(leaveSupervisorArrayAdaptor)

        var selectedLeaveType: String? = null
        viewModel.leaveType.observe(viewLifecycleOwner, Observer { leaveTypes ->
            binding.autoCompleteTextViewLeaveType.setOnItemClickListener { parent, view, position, id ->
                selectedLeaveType = parent.getItemAtPosition(position) as String?
                if (selectedLeaveType.toString() == "Annual Leave") {
                    binding.textViewAvailableLeave.text =
                        selectedLeaveType.toString() + " left: " + leaveTypes[0].annualLeaveBalance.toString() + " Days"
                } else if (selectedLeaveType.toString() == "Sick Leave") {
                    binding.textViewAvailableLeave.text =
                        selectedLeaveType.toString() + " left: " + leaveTypes[0].sickLeaveBalance.toString() + " Days"
                } else {
                    binding.textViewAvailableLeave.text =
                        selectedLeaveType.toString() + " left: " + leaveTypes[0].maternityLeaveBalance.toString() + " Days"
                }
            }
        })

//        var datePickerStartDateMonth: String? = null
//        var datePickerStartDateDay: String? = null
//        var datePickerEndDateMonth: String? = null
//        var datePickerEndDateDay: String? = null
//
        // Submit button
        binding.buttonSubmitLeave.setOnClickListener {
            // validation for date picker
            var datePickerStartDate = binding.textViewLeaveStartDate.text.toString()
//            datePickerStartDateMonth = datePickerStartDate.split("/")[1]
//            datePickerStartDateDay = datePickerStartDate.split("/")[0]

            var datePickerEndDate = binding.textViewLeaveEndDate.text.toString()
//            datePickerEndDateMonth = datePickerEndDate.split("/")[1]
//            datePickerEndDateDay = datePickerEndDate.split("/")[0]


            if (binding.textViewLeaveStartDate.text.isNullOrBlank() || binding.textViewLeaveEndDate.text.isNullOrBlank()
                || binding.autoCompleteTextViewLeaveType.text.isNullOrBlank() || selectedDay == "0" || binding.autoCompleteTextViewLeaveSupervisor.text.isNullOrBlank()) {
                Toast.makeText(
                    this@LeaveApplyFragment.requireActivity(),
                    "Please fill in all the fields before you submit!", Toast.LENGTH_SHORT
                ).show()
            } else if ((datePickerEndDate.split("/")[0] < datePickerStartDate.split("/")[0]) &&
                (datePickerEndDate.split("/")[1] <= datePickerStartDate.split("/")[1])
            ) {
                Toast.makeText(
                    this@LeaveApplyFragment.requireActivity(),
                    "Start Date cannot be later than End Date!", Toast.LENGTH_SHORT
                ).show()
            } else {

                saveLeave()

                if (selectedLeaveType.toString() == "Annual Leave") {
                    viewModel.updateAnnualLeaveBalance()
                } else if (selectedLeaveType.toString() == "Sick Leave") {
                    viewModel.updateSickLeaveBalance()
                } else {
                    viewModel.updateMaternityLeaveBalance()
                }

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveFragment())
                    .commitNow()
//            }
            }
        }

        binding.buttonCancelLeave.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, LeaveFragment())
                .commitNow()
        }
    }


        override fun onResume() {
            super.onResume()
            (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
        }

        override fun onStart() {
            super.onStart()
            (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        internal fun saveLeave(){
            storeLeave()
            viewModel.save(leave)
            leave = Leave()

        }

        internal fun storeLeave(){
            leave.apply {
                leaveType = binding.autoCompleteTextViewLeaveType.text.toString()
                leaveStartDate = binding.textViewLeaveStartDate.text.toString()
                leaveEndDate = binding.textViewLeaveEndDate.text.toString()
                leaveDay = selectedDay
                leaveSupervisor = binding.autoCompleteTextViewLeaveSupervisor.text.toString()
                leaveStatus = "Pending"

            }
        }

    }
