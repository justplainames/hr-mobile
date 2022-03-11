package edu.singaporetech.hr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import edu.singaporetech.hr.databinding.FragmentLeaveApplyBinding
import edu.singaporetech.hr.databinding.FragmentLeaveRecordBinding
import edu.singaporetech.hr.leave.LeaveRecordAdaptor
import edu.singaporetech.hr.leave.LeaveRecordItem
import edu.singaporetech.hr.leave.LeaveRecordViewAllAdaptor
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem


class LeaveRecordFragment : Fragment() {

    private lateinit var leaveArrayList: ArrayList<LeaveRecordViewAllItem>

    private lateinit var leaveRecordAdapter: LeaveRecordViewAllAdaptor
    private lateinit var leaveRecordRecyclerView: RecyclerView
    private lateinit var binding: FragmentLeaveRecordBinding
    private val viewModel: LeaveRecordViewModel by viewModels()
    private var leaveRecord = LeaveRecordViewAllItem()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(
               inflater,
        R.layout.fragment_leave_record,container, false
        )

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

        leaveRecordRecyclerView = binding.recyclerViewLeaveRecordViewAll
        leaveRecordRecyclerView.layoutManager = LinearLayoutManager(activity)
        leaveRecordRecyclerView.setHasFixedSize(true)

        leaveArrayList = arrayListOf()
        leaveRecordAdapter = LeaveRecordViewAllAdaptor(leaveArrayList)
        leaveRecordRecyclerView.adapter = leaveRecordAdapter

        viewModel.leave.observe(viewLifecycleOwner, Observer {
                leave ->
            leaveArrayList.removeAll(leaveArrayList)
            leaveArrayList.addAll(leave)
            leaveRecordRecyclerView.adapter!!.notifyDataSetChanged()
        })

        viewModel.fetchItems()
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

private fun <E> ArrayList<E>.addAll(elements: ArrayList<Leave>?) {

}
