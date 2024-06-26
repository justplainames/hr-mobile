package edu.singaporetech.hr.fragment.attendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.ContentValues
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import edu.singaporetech.hr.data.AttendanceItem
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.AttendanceClockViewModel
import edu.singaporetech.hr.databinding.FragmentAttendanceClockBinding
import java.util.*

/*
    AttendanceClockFragment : Attendance Clock Fragment
        -- obtain the location of the user
           - request for permissions
        -- authentication for clock in/ clock out
            - biometric (fingerprint, facial recognition)
        -- Get status of clock in/ clock out from the firebase
            - update when the user click on the clock in/clock out button
 */
class AttendanceClockFragment : Fragment(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var locationPermissionGranted = false

    // A default location (Google Asia Pacific, Singapore) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(1.2765, 103.7992)

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentAttendanceClockBinding

    private val viewModel: AttendanceClockViewModel by viewModels()
    private var clockStatus = AttendanceItem()
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
                    // Saves attendance status and timing, update clock in status and resets location text view
                    if (viewModel.clockInStatus.value == true) {
                        saveAttendanceClockIn()

                        binding.locationTextView.text = ""
                    } else {
                        saveAttendanceClockOut()
                        viewModel.clockInStatus.value = true
                        binding.locationTextView.text = ""

                    }
                }
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_attendance_clock, container, false
        )

        /**
         * Observe for any changes in clock in status in the database to update button layout
         *  - Change button to clock in if clock status = true, else button will be clock out
         */
        viewModel.clockInStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer { status ->
            if (status) {
                binding.attendanceClockBtn.setBackgroundResource(R.drawable.clockin_button)
                binding.attendanceClockBtn.setText(R.string.attendance_clockIn)
            } else {
                binding.attendanceClockBtn.setBackgroundResource(R.drawable.clockout_button)
                binding.attendanceClockBtn.setText(R.string.attendance_clockOut)

            }
        })

        // Create a new instance of FusedLocationProviderClient for use in this fragment activity
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

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

        // Creates a map fragment and sets a callback object when the GoogleMap instance is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.attendanceMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val btnGetLocation = binding.getLocationButton

        // When user click on Get Location button, request for location permission, set location controls
        // on the map and show current location on map
        btnGetLocation.setOnClickListener {
            getLocationPermission()
            updateLocationUI()
            getLocation()
        }

        // Checks if location has been obtained and asks for biometric authentication when
        // location is obtained before clocking in/out
        binding.attendanceClockBtn.setOnClickListener {
            if (binding.locationTextView.text.isNullOrBlank()) {
                Toast.makeText(
                    this@AttendanceClockFragment.requireActivity(),
                    "Location Missing, Please click on GET LOCATION before you submit!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                biometricAuthentication()
            }
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

    internal fun saveAttendanceClockIn() {
        Log.d("TESTING", "saveAttendanceIn()")
        storeAttendanceClockIn()
        viewModel.save(clockStatus)
        clockStatus = AttendanceItem()
        Log.d("TESTING", "saveAttendanceIn1()")
    }

    internal fun saveAttendanceClockOut() {
        Log.d("TESTING", "saveAttendanceOUT()")
        val clockOutDate = Calendar.getInstance().time
        val clockOutAddress = binding.locationTextView.text as String?
        Log.d("TESTING", "saveAttendanceOUT1()")

        Log.d("TESTING", "saveAttendanceOUT3()")
        if (clockOutAddress != null) {
            viewModel.updateAttendanceRecord(clockOutDate, clockOutAddress)
        }
        Log.d("TESTING", "saveAttendanceOUT3()")

        clockStatus = AttendanceItem()

        Log.d(TAG, "saveAttendanceOut()4")
    }

    private fun storeAttendanceClockIn() {
        clockStatus.apply {
            val clockInTime = Calendar.getInstance()
            clockInDate = clockInTime.time
            val hours = clockInTime.get(Calendar.HOUR_OF_DAY)
            Log.d("TESTINGgggg", clockInTime.toString())
            Log.d("TESTINGgggg", clockInDate.toString())
            Log.d("TESTINGgggg", hours.toString())
            clockInAddress = binding.locationTextView.text as String?

            // Sets attendance status to on time if user clock in before 9am, else user is late
            if (hours < 9) {
                attendanceStatus = "On Time"
                viewModel.updateAttendanceStatusOnTime()

            } else {
                attendanceStatus = "Late"
                viewModel.updateAttendanceStatusLate()
            }
        }
    }

    private fun getLocation() {
        /*
         * Get the most recent location currently available, then set map to the location.
         * If current location unavailable, set map to default location.
         */
        val txtLocation = binding.locationTextView
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                        // Get location address using latitude and longitude
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude,
                            1
                        )
                        txtLocation.text = addresses.get(0).getAddressLine(0)
                    } else {
                        Log.d(ContentValues.TAG, "Current location is null. Using defaults.")
                        Log.e(ContentValues.TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }

    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        /*
         * Set the location controls on the map. If user has granted location permission,
         * enable the My Location layer and related control on the map. Otherwise disable
         * the layer and the control, and set the current location to null
         */
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager =
            requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it.applicationContext,
                    Manifest.permission.USE_BIOMETRIC
                )
            } != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun biometricAuthentication() {
        checkBiometricSupport()
        val biometricPrompt = activity?.let { it1 ->
            BiometricPrompt.Builder(activity)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
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

    /**
     * Google Maps parameters
     */
    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        getLocationPermission()
        updateLocationUI()
        getLocation()
    }
}