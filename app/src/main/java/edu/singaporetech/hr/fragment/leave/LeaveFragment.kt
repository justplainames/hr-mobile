package edu.singaporetech.hr.fragment.leave

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.data.Leave
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.LeaveViewModel
import edu.singaporetech.hr.databinding.FragmentLeaveBinding
import edu.singaporetech.hr.fragment.HomeFragment
import edu.singaporetech.hr.adapter.LeaveRecordAdaptor


class LeaveFragment : Fragment() {

    private lateinit var leaveArrayList: ArrayList<Leave>
    private lateinit var binding: FragmentLeaveBinding
    private lateinit var leaveAdapter: LeaveRecordAdaptor
    private lateinit var leaveRecyclerView: RecyclerView
    private val viewModel: LeaveViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leave, container, false
        )

        var annual:Int
        var annualCurr:Int
        var maternity:Int
        var maternityCurr:Int
        var sick:Int
        var sickCurr:Int

        viewModel.leaveType.observe(viewLifecycleOwner, Observer {
                leaveTypes ->
            binding.textViewAnnualLeaveBalance.setText(leaveTypes[0].annualLeaveBalance.toString() + " / " + leaveTypes[0].annualLeaveTotal.toString())
            binding.textViewSickLeaveBalance.setText(leaveTypes[0].sickLeaveBalance.toString() + " / " + leaveTypes[0].sickLeaveTotal.toString())
            binding.textViewMaternityLeaveBalance.setText(leaveTypes[0].maternityLeaveBalance.toString() + " / " + leaveTypes[0].maternityLeaveTotal.toString())

            // Progress bar
            annual = leaveTypes[0].annualLeaveTotal.toInt()
            binding.progressBarAnnualLeave.max = annual
            annualCurr = leaveTypes[0].annualLeaveBalance.toInt()

            ObjectAnimator.ofInt(binding.progressBarAnnualLeave,"progress",annualCurr)
                .setDuration(10)
                .start()

            sick = leaveTypes[0].sickLeaveTotal.toInt()
            binding.progressBarSickLeave.max = sick
            sickCurr = leaveTypes[0].sickLeaveBalance.toInt()

            ObjectAnimator.ofInt(binding.progressBarSickLeave,"progress",sickCurr)
                .setDuration(10)
                .start()

            maternity = leaveTypes[0].maternityLeaveTotal.toInt()
            binding.progressBarMaternityLeave.max = maternity
            maternityCurr = leaveTypes[0].maternityLeaveBalance.toInt()

            ObjectAnimator.ofInt(binding.progressBarMaternityLeave,"progress",maternityCurr)
                .setDuration(10)
                .start()
        })

        leaveRecyclerView = binding.recyclerViewLeaveRecord
        leaveRecyclerView.layoutManager = LinearLayoutManager(activity)
        leaveRecyclerView.setHasFixedSize(true)
        leaveRecyclerView.itemAnimator = DefaultItemAnimator()

        leaveArrayList = arrayListOf()
        leaveAdapter = LeaveRecordAdaptor(leaveArrayList)
        leaveRecyclerView.adapter = leaveAdapter

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            leave ->
            leaveArrayList.removeAll(leaveArrayList)
            leaveArrayList.addAll(leave)
            leaveRecyclerView.adapter!!.notifyDataSetChanged()
        })

        binding.buttonApplyLeave.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, LeaveApplyFragment())
                .commitNow()}

        binding.buttonViewAllLeave.setOnClickListener { requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, LeaveRecordFragment())
            .commitNow()}

        return binding.root

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