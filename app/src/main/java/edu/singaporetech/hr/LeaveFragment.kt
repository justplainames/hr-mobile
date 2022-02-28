package edu.singaporetech.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.singaporetech.hr.databinding.FragmentLeaveBinding
import edu.singaporetech.hr.leave.LeaveRecordAdaptor
import edu.singaporetech.hr.leave.LeaveRecordItem


class LeaveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentLeaveBinding>(
            inflater,
            R.layout.fragment_leave, container, false
        )

        val leaveRecordList = generateDummyDataList(10)

        binding.recyclerViewLeaveRecord.adapter = LeaveRecordAdaptor(leaveRecordList)
        binding.recyclerViewLeaveRecord.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewLeaveRecord.setHasFixedSize(true)

        binding.buttonApplyLeave.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, LeaveApplyFragment())
                .commitNow()}


        binding.buttonViewAllLeave.setOnClickListener { requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView,LeaveRecordFragment())
            .commitNow()}

        return binding.root

    }
    private fun generateDummyDataList(size:Int): List<LeaveRecordItem>{

        val list = ArrayList<LeaveRecordItem>()
        for (i in 0 until size){
            val item = LeaveRecordItem("Annual", "$i","21/2","2.0","Accepted")
            list +=item
        }
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object:OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Home"
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
        (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
    }



}