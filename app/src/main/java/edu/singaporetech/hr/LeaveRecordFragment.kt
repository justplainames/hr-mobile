package edu.singaporetech.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.singaporetech.hr.databinding.FragmentLeaveRecordBinding
import edu.singaporetech.hr.leave.LeaveRecordAdaptor
import edu.singaporetech.hr.leave.LeaveRecordItem


class LeaveRecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = DataBindingUtil.inflate<FragmentLeaveRecordBinding>(
            inflater,
            R.layout.fragment_leave_record, container, false
        )

        val leaveRecordList = generateDummyDataList(10)

        binding.recyclerViewLeaveRecordViewAll.adapter = LeaveRecordAdaptor(leaveRecordList)
        binding.recyclerViewLeaveRecordViewAll.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewLeaveRecordViewAll.setHasFixedSize(true)

//        val button = view.findViewById<Button>(R.id.buttonViewAllLeave)
//        button.setOnClickListener {
//            findNavController().navigate(R.id.action_leaveFragment_to_leaveRecordFragment)
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveFragment())
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

    private fun generateDummyDataList(size:Int): List<LeaveRecordItem>{

        val list = ArrayList<LeaveRecordItem>()
        for (i in 0 until size){
            val item = LeaveRecordItem("Annual", "$i","21/2","2.0","Accepted")
            list +=item
        }
        return list
    }

}