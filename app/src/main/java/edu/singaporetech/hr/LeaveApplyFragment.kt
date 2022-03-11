package edu.singaporetech.hr

import android.app.DatePickerDialog
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.singaporetech.hr.databinding.FragmentLeaveApplyBinding

class LeaveApplyFragment : Fragment() {

    private lateinit var binding: FragmentLeaveApplyBinding
    private lateinit var db: FirebaseFirestore
//    private val viewModel = ViewModelProvider(this).get(LeaveViewModel::class.java)
    private val viewModel: LeaveViewModel by viewModels()
    private var leave = Leave()

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object:
            OnBackPressedCallback(true){

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
        val leaveTypeArrayAdaptor = ArrayAdapter(requireContext(),R.layout.leavetype_dropdown_item, dropDownLeaveType)
        binding.autoCompleteTextViewLeaveType.setAdapter(leaveTypeArrayAdaptor)


        // Date Picker for Start Date
        binding.textViewLeaveStartDate.setOnClickListener{
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                val date = bundle.getString("SELECTED_DATE")
                binding.textViewLeaveStartDate.text = date
            }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        // Date Picker for End Date
        binding.textViewLeaveEndDate.setOnClickListener{
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                val date = bundle.getString("SELECTED_DATE")
                binding.textViewLeaveEndDate.text = date
            }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        // Radio Group for Leave Day
        binding.radioGroupLeaveDay.setOnCheckedChangeListener { group, checkedId ->

//            if(binding.radioButtonFullDay.id == checkedId) {
//                "Full Day"
//            }else{
//                "Half Day"
//            }
        }

        // Spinner for Leave Type
        val dropDownLeaveSupervisor = resources.getStringArray(R.array.leaveSupervisor)
        val leaveSupervisorArrayAdaptor = ArrayAdapter(requireContext(),R.layout.leave_supervisor_dropdown_item, dropDownLeaveSupervisor)
        binding.autoCompleteTextViewLeaveSupervisor.setAdapter(leaveSupervisorArrayAdaptor)

        // Submit button
        binding.buttonSubmitLeave.setOnClickListener {
            saveLeave()
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
                //leaveday = binding.radioGroupLeaveDay.text.toString()

            }
        }
    }
