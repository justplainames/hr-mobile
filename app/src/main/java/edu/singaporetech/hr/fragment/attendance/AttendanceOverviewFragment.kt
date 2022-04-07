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
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.*
import edu.singaporetech.hr.ViewModel.AttendanceClockViewModel
import edu.singaporetech.hr.ViewModel.AttendanceViewModel
import edu.singaporetech.hr.adapter.AttendanceAdapter
import edu.singaporetech.hr.databinding.AttendanceOverviewBinding
import edu.singaporetech.hr.fragment.HomeFragment
import java.time.LocalDateTime
//import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import java.util.ArrayList

class AttendanceOverviewFragment: Fragment(), AttendanceAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewViewModel: AttendanceViewModel
    private lateinit var viewClockModel: AttendanceClockViewModel
    private lateinit var attendancedapter: AttendanceAdapter
    private lateinit var attendanceArrayList: ArrayList<Attendance>
    private lateinit var attendanceRecyclerView: RecyclerView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AttendanceOverviewBinding>(
            inflater, R.layout.attendance_overview, container, false
        )
        getActivity()?.getViewModelStore()?.clear()
        viewViewModel = ViewModelProvider(requireActivity()).get(AttendanceViewModel::class.java)

        val attendanceListObserver = Observer<ArrayList<Attendance>> { items->
            attendancedapter= AttendanceAdapter(items.take(items.size) as ArrayList<Attendance>,this) // add items to adapter
            binding.recyclerViewAttendence.adapter=attendancedapter
        }

        viewViewModel.attendance.observe(requireActivity(), attendanceListObserver)

        binding.recyclerViewAttendence.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewAttendence.setHasFixedSize(true)

        viewClockModel = ViewModelProvider(requireActivity()).get(AttendanceClockViewModel::class.java)

        viewClockModel.attendanceStatus.observe(viewLifecycleOwner, Observer { summary ->
            Log.d("TESTING", "saveAttendanceIn()")
            binding.textViewOnTime.setText(summary[1].onTime.toString())
            binding.textViewLate.setText(summary[1].late.toString())
            var miss = LocalDateTime.now().dayOfMonth - summary[1].onTime - summary[1].late
            binding.textViewAbsent.setText(miss.toString())
            var onTime: Float = summary[1].onTime?.toFloat() ?: 0.0f
            var late: Float =summary[1].late?.toFloat() ?: 0.0f
            var total:Float= onTime+late
            val value:Float= ((onTime) / (total)) *360f
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
//            val formatValue = String.format("%.0f", summary.percentageMissed * 100)
//
//            val arrayListChart = ArrayList<Int>()
//            arrayListChart.add(Integer.valueOf(summary[1].onTime.toInt()))
//            arrayListChart.add(Integer.valueOf(summary[1].late.toInt()))
//            arrayListChart.add(Integer.valueOf(miss.toInt()))
            //arrayListChart.add(Integer.valueOf(summary.daysOnLeave))
//
//            val s1 = Segment("On Time", arrayListChart.get(0))
//            val s2 = Segment("Late", arrayListChart.get(1))
//            val s3 = Segment("Absent", arrayListChart.get(2))
//            val s4 = Segment("On Time", arrayListChart.get(0))



            binding.clockOutBtn.setOnClickListener({
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                    .commitNow()
            })


        })

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