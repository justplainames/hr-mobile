package edu.singaporetech.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.singaporetech.hr.databinding.AttendanceOverviewBinding
import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import java.util.ArrayList

class AttendanceOverviewFragment: Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: AttendanceModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AttendanceOverviewBinding>(
            inflater,R.layout.attendance_overview, container, false
        )

        binding.allAttendanceBtn.setOnClickListener({
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceFragment())
                .commitNow()
        })

        binding.clockInBtn.setOnClickListener({
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                .commitNow()
        })

        binding.clockOutBtn.setOnClickListener({
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                .commitNow()
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



//    override fun onItemClickNext(position: Int) {
//        requireActivity()
//            .supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragmentContainerView, PayslipDetailFragment(position))
//            .commitNow()
//    }

//


}