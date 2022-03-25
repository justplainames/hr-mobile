package edu.singaporetech.hr

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.hr.databinding.FragmentLeaveApplyBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class LeaveApplyFragment : Fragment() {

    private lateinit var binding: FragmentLeaveApplyBinding
//    private val viewModel = ViewModelProvider(this).get(LeaveViewModel::class.java)
    private val viewModel: LeaveViewModel by viewModels()
    private var leave = Leave()
    var selectedDay:String? = null

    var image_uri: Uri? = null
    private val CAMERA_PERMISSION_CODE:Int = 1000
    private val IMAGE_CAPTURE_CODE:Int =  1001

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
            var datePickerEndDate = binding.textViewLeaveEndDate.text.toString()

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
                val dialogView = layoutInflater.inflate(R.layout.leave_apply_dialog, null)
                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle("Confirmation Message")
                val alertDialog = dialogBuilder.show()
                val confirmYesButton = dialogView.findViewById<Button>(R.id.buttonYesApplyLeave)
                val confirmNoButton = dialogView.findViewById<Button>(R.id.buttonNoApplyLeave)

                confirmYesButton.setOnClickListener {
                    alertDialog.dismiss()

                    saveLeave()
                    uploadImage()

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
                }
                confirmNoButton.setOnClickListener {
                    alertDialog.dismiss()
                }


            }
        }

        binding.buttonCancelLeave.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, LeaveFragment())
                .commitNow()
        }

        binding.buttonCamera.setOnClickListener {
            val permissionGranted = requestCameraPermission()
            if (permissionGranted){
                requestCameraPermission()
            }
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
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val fileName = formatter.format(now)

            image_uri?.let { viewModel.save(leave, fileName, it) }
            leave = Leave()

        }

        internal fun storeLeave(){
            leave.apply {
                leaveType = binding.autoCompleteTextViewLeaveType.text.toString()
                leaveStartDate = binding.textViewLeaveStartDate.text.toString()
                leaveEndDate = binding.textViewLeaveEndDate.text.toString()
                leaveDay = selectedDay
                leaveSupervisor = binding.autoCompleteTextViewLeaveSupervisor.text.toString()
                leaveReason = binding.editTextLeaveReason.text.toString()
                leaveStatus = "Pending"

            }
        }



        private fun requestCameraPermission(): Boolean {
            var permissionGranted = false
            // If system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val cameraPermissionNotGranted = checkSelfPermission(
                    activity as MainActivity,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
                if (checkSelfPermission(
                        activity as MainActivity,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(android.Manifest.permission.CAMERA)
                    // Display permission dialog
                    requestPermissions(permission, CAMERA_PERMISSION_CODE)
                } else {
                    // Permission already granted
                    permissionGranted = true
                    openCamera()
                }
            } else {
                // Android version earlier than M -&gt; no need to request permission
                permissionGranted = true
                openCamera()
            }
            return permissionGranted
    }



    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Pic")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From cam")
        image_uri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)
    }

    private fun uploadImage(){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)

        image_uri?.let { viewModel.saveImage(leave.leaveId, fileName, it) }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
         when(requestCode){
             CAMERA_PERMISSION_CODE -> {
                 if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                     openCamera()
                 }else{
                     Toast.makeText(this@LeaveApplyFragment.requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                 }
             }
         }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){

        }
    }

}
