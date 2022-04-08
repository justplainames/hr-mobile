package edu.singaporetech.hr.fragment.attendance

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.Attendance
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.AttendanceClockViewModel
import edu.singaporetech.hr.ViewModel.AttendanceViewModel
import edu.singaporetech.hr.adapter.AttendanceAdapter
import edu.singaporetech.hr.databinding.FragmentAttendanceOverviewBinding
import edu.singaporetech.hr.fragment.HomeFragment
import java.time.LocalDateTime

class AttendanceOverviewFragment: Fragment(), AttendanceAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewViewModel: AttendanceViewModel
    private lateinit var viewClockModel: AttendanceClockViewModel
    private lateinit var attendancedapter: AttendanceAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentAttendanceOverviewBinding>(
            inflater, R.layout.fragment_attendance_overview, container, false
        )
        activity?.viewModelStore?.clear()
        viewViewModel = ViewModelProvider(requireActivity())[AttendanceViewModel::class.java]

        val attendanceListObserver = Observer<ArrayList<Attendance>> { items->
            attendancedapter= AttendanceAdapter(items.take(items.size) as ArrayList<Attendance>,this) // add items to adapter
            binding.recyclerViewAttendence.adapter=attendancedapter
        }

        viewViewModel.attendance.observe(requireActivity(), attendanceListObserver)

        binding.recyclerViewAttendence.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewAttendence.setHasFixedSize(true)

        viewClockModel = ViewModelProvider(requireActivity())[AttendanceClockViewModel::class.java]

        viewClockModel.attendanceStatus.observe(viewLifecycleOwner) { summary ->
            Log.d("TESTING", "saveAttendanceIn()")
            binding.textViewOnTime.text = summary[1].onTime.toString()
            binding.textViewLate.text = summary[1].late.toString()
            val miss = LocalDateTime.now().dayOfMonth - summary[1].onTime - summary[1].late
            binding.textViewAbsent.text = miss.toString()
            val onTime: Float = summary[1].onTime.toFloat()
            val late: Float = summary[1].late.toFloat()
            val total: Float = onTime + late
            val value: Float = ((onTime) / (total)) * 360f
            val circularProgressBar = binding.circularProgressBar
            circularProgressBar.apply {

                setProgressWithAnimation(value, 2000) // =1s
                // Set Progress Max
                progressMax = 360f
                // Set ProgressBar Color
                progressBarColor = Color.GREEN
                // Set background ProgressBar Color
                backgroundProgressBarColor = Color.GRAY
                // or with gradient
                backgroundProgressBarColorStart = Color.RED
                backgroundProgressBarColorEnd = Color.RED
                backgroundProgressBarColorDirection =
                    CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

                // Set Width
                progressBarWidth = 15f // in DP
                backgroundProgressBarWidth = 15f // in DP

                // Other
                roundBorder = true
                startAngle = 0f
                progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
            }


            binding.clockOutBtn.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                    .commitNow()
            }


        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "HomePage"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomeFragment())
                    .commitNow()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance Overview"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance Overview"
    }

    override fun onItemClick(position: Int, clockInDate: String, id: String) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, ReportAttendanceFragment(position,clockInDate,id))
            .commitNow()
    }

}