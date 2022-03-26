package edu.singaporetech.hr

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
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

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication success!")
                    if (binding.attendanceclockBtn.text == getText(R.string.clockInText)) {
                        saveAttendanceClockIn()
                        binding.locationTextView.text = ""
                        binding.attendanceclockBtn.setBackgroundResource(R.drawable.clockout_button)
                        binding.attendanceclockBtn.setText(R.string.clockOutText)
                    }
                    else{
                        saveAttendanceClockOut()
                        binding.locationTextView.text = ""
                        binding.attendanceclockBtn.setBackgroundResource(R.drawable.clockin_button)
                        binding.attendanceclockBtn.setText(R.string.clockInText)
                    }
                }
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_attendance_clock,container, false
        )

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Attendance Overview"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                    .commitNow()
            }
        })

        var btnGetLocation = binding.getLocationButton

        btnGetLocation.setOnClickListener {
            getLocation()
        }

        // Submit button
        binding.attendanceclockBtn.setOnClickListener {
            biometricAuthentication()
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

    internal fun saveAttendanceClockIn(){
        storeAttendanceClockIn()
        viewModel.save(clockstatus)
        clockstatus = AttendanceItem()
        Log.d(TAG, "saveAttendanceIn()")
    }

    internal fun saveAttendanceClockOut(){
        var clockoutDate = Calendar.getInstance().time
        var clockoutAddress = binding.locationTextView.text as String?

        if (clockoutAddress != null) {
            viewModel.update(viewModel.documentId, clockoutDate, clockoutAddress)
        }
        clockstatus = AttendanceItem()
        Log.d(TAG, "saveAttendanceOut()")
    }

    internal fun storeAttendanceClockIn(){
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

    private fun notifyUser(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal() : CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (activity?.let { ActivityCompat.checkSelfPermission(it.applicationContext, android.Manifest.permission.USE_BIOMETRIC) } != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        } else true
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun biometricAuthentication(){
        checkBiometricSupport()
        val biometricPrompt = activity?.let { it1 ->
            BiometricPrompt.Builder(activity).setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .setTitle("Attendance Authentication!")
                .setSubtitle("Use biometrics to Authenticate")
                .setNegativeButton(
                    "Cancel",
                    it1.mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                        notifyUser("Authentication Cancelled")
                    }).build()
        }

        if (biometricPrompt != null) {
            biometricPrompt.authenticate(
                getCancellationSignal(),
                requireContext().mainExecutor,
                authenticationCallback
            )
        }
    }

}