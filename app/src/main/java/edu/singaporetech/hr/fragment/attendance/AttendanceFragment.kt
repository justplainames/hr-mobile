package edu.singaporetech.hr.fragment.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.*
import edu.singaporetech.hr.ViewModel.AttendanceViewModel
import edu.singaporetech.hr.adapter.AttendanceAdapter
import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
//import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import java.util.ArrayList

class AttendanceFragment : Fragment(), AttendanceAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewViewModel: AttendanceViewModel
    private lateinit var attendancedapter: AttendanceAdapter
    private lateinit var attendanceArrayList: ArrayList<Attendance>
    private lateinit var attendanceRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAttendanceBinding>(
            inflater, R.layout.fragment_attendance, container, false
        )
        viewViewModel = ViewModelProvider(requireActivity()).get(AttendanceViewModel::class.java)

        val attendanceListObserver = Observer<ArrayList<Attendance>> { items->
            attendancedapter= AttendanceAdapter(items.take(items.size) as ArrayList<Attendance>,this) // add items to adapter
            binding.recyclerViewAttendence.adapter=attendancedapter
        }

        viewViewModel.attendance.observe(requireActivity(), attendanceListObserver)

        binding.recyclerViewAttendence.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewAttendence.setHasFixedSize(true)

        //val ref = FirebaseFirestore.getInstance()


//        attendanceArrayList = arrayListOf()
//        attendancedapter = AttendanceAdapter(attendanceArrayList,this)
//        attendanceRecyclerView.adapter = attendancedapter
//
//        viewModel.attendance.observe(viewLifecycleOwner, Observer {
//                attendance ->
//            attendanceArrayList.removeAll(attendanceArrayList)
//            attendanceArrayList.addAll(attendance)
//            attendanceRecyclerView.adapter!!.notifyDataSetChanged()
//        })


        binding.attendanceSummaryBtn.setOnClickListener({
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                .commitNow()
        })

        return binding.root
    }


//        binding.clockInBtn.setOnClickListener({
//            requireActivity()
//                .supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragmentContainerView, AttendanceClockFragment())
//                .commitNow()
//        })




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Attendance Overview"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                    .commitNow()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance"
    }

    override fun onItemClick(position: Int, clockInDate: String, id: String) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, ReportAttendanceFragment(position,clockInDate,id))
            .commitNow()
    }


}