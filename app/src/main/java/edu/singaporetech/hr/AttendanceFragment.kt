package edu.singaporetech.hr

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
import com.google.firebase.firestore.*
import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem
import java.util.*


class AttendanceFragment : Fragment(), AttendanceAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: AttendanceModel
    private lateinit var adapter : AttendanceAdapter
    private lateinit var attendancedapter: AttendanceAdapter
    private lateinit var attendanceArrayList: ArrayList<Attendance>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAttendanceBinding>(
            inflater,R.layout.fragment_attendance, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(AttendanceModel::class.java)
        //getActivity()?.getViewModelStore()?.clear()
        val attendanceListObserver = Observer<ArrayList<Attendance>> { items->

           // viewModel.attendenceArrayList.clear()
            adapter=AttendanceAdapter(items,this) // add items to adapter
           // binding.recyclerViewAttendence.invalidate();

            adapter.notifyDataSetChanged()
            binding.recyclerViewAttendence.adapter=adapter
        }

        viewModel.attendance.observe(requireActivity(), attendanceListObserver)

//        viewModel.attendance.observe(viewLifecycleOwner, Observer {
//                attendance ->
//            leaveArrayList.removeAll(leaveArrayList)
//            leaveArrayList.addAll(leave)
//            leaveRecordRecyclerView.adapter!!.notifyDataSetChanged()
//        })


        binding.recyclerViewAttendence.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewAttendence.setHasFixedSize(true)

        binding.attendanceSummaryBtn.setOnClickListener({
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                .commitNow()
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:OnBackPressedCallback(true){

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

    override fun onItemClick(position: Int,clockInDate: String, id: String) {

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, ReportAttendanceFragment(position,clockInDate,id))
            .commitNow()
    }




//


}


