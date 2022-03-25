package edu.singaporetech.hr

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.singaporetech.hr.databinding.FragmentAttendanceClockBinding
import java.util.*

class AttendanceClockFragment : Fragment() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentAttendanceClockBinding

    private val viewModel: AttendanceClockViewModel by viewModels()
    private var clockstatus = AttendanceItem()
    private val TAG = "AttendanceClockActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_attendance_clock,container, false
        )

        var btnGetLocation = binding.getLocationButton

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        btnGetLocation.setOnClickListener {
            getLocation()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
//                (requireActivity() as MainActivity).supportActionBar?.title = "Attendance"
//                requireActivity()
//                    .supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fragment_container_view_tag, attendanceFragment())
//                    .commitNow()
            }
        })

        // Submit button
        binding.attendanceclockBtn.setOnClickListener {
            saveAttendance()
        }
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Clock Attendance"
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Clock Attendance"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    internal fun saveAttendance(){
        storeAttendance()
        viewModel.save(clockstatus)
        clockstatus = AttendanceItem()
        Log.d(TAG, "saveAttendance()")
    }

    internal fun storeAttendance(){
        clockstatus.apply {
            clockInDate = Calendar.getInstance().time
            clockInAddress = binding.locationTextView.text as String?
        }
    }

    private fun getLocation(){
        var txtLocation = binding.locationTextView
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                44
            )
        }
        else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null){
                        var geocoder = Geocoder(requireContext(), Locale.getDefault())
                        var addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        txtLocation.text = addresses.get(0).getAddressLine(0)
                    }
                    else{

                    }
                }
        }
    }

}