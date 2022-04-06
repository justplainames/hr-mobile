package edu.singaporetech.hr

import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import edu.singaporetech.hr.databinding.AttendanceOverviewBinding
import java.time.LocalDateTime
//import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import java.util.ArrayList

class AttendanceOverviewFragment: Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: AttendanceModel
    private lateinit var viewClockModel: AttendanceClockViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AttendanceOverviewBinding>(
            inflater,R.layout.attendance_overview, container, false
        )

        viewClockModel = ViewModelProvider(requireActivity()).get(AttendanceClockViewModel::class.java)

        viewClockModel.attendanceStatus.observe(viewLifecycleOwner, Observer { summary ->
            Log.d("TESTING", "saveAttendanceIn()")
            binding.textViewOnTime.setText(summary[1].onTime.toString())
            binding.textViewLate.setText(summary[1].late.toString())
//            if (summary.daysMissed in 6..8){
//                binding.textViewAbsent.setTextColor(
//                    ContextCompat.getColor(
//                    requireContext(),R.color.orange))
//            } else if (summary.daysMissed > 8 ){
//                binding.textViewAbsent.setTextColor(
//                    ContextCompat.getColor(
//                    requireContext(),R.color.red))}
//            else {
//                binding.textViewAbsent.setTextColor(
//                    ContextCompat.getColor(
//                    requireContext(),R.color.main))}
            var miss = LocalDateTime.now().dayOfMonth - summary[1].onTime - summary[1].late
            binding.textViewAbsent.setText(miss.toString())


//            val formatValue = String.format("%.0f", summary.percentageMissed * 100)

            val arrayListChart = ArrayList<Int>()
            arrayListChart.add(Integer.valueOf(summary[1].onTime.toInt()))
            arrayListChart.add(Integer.valueOf(summary[1].late.toInt()))
            arrayListChart.add(Integer.valueOf(miss.toInt()))
            //arrayListChart.add(Integer.valueOf(summary.daysOnLeave))

            val s1 = Segment("On Time", arrayListChart.get(0))
            val s2 = Segment("Late", arrayListChart.get(1))
            val s3 = Segment("Absent", arrayListChart.get(2))
//            val s4 = Segment("On Time", arrayListChart.get(0))

            val sf1 = SegmentFormatter(Color.parseColor("#2BFBB8"))
            val sf2 = SegmentFormatter(Color.parseColor("#FB502B"))
            val sf3 = SegmentFormatter(Color.parseColor("#FFC300"))

            binding.circularProgressBar.addSegment(s1,sf1)
            binding.circularProgressBar.addSegment(s2,sf2)
            binding.circularProgressBar.addSegment(s3,sf3)
            binding.circularProgressBar.getBorderPaint().setColor(Color.TRANSPARENT)
            binding.circularProgressBar.getBackgroundPaint().setColor(Color.TRANSPARENT)

            binding.clockOutBtn.setOnClickListener({
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                    .commitNow()
            })

            binding.allAttendanceBtn.setOnClickListener({
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceFragment())
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


}