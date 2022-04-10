package edu.singaporetech.hr.fragment.leave

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.data.Leave
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.LeaveRecordViewModel

import edu.singaporetech.hr.databinding.FragmentLeaveRecordBinding
import edu.singaporetech.hr.adapter.LeaveRecordViewAllAdaptor
import edu.singaporetech.hr.data.LeaveRecordViewAllItem


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

        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        leaveRecordAdapter.setOnItemClickListener(object: LeaveRecordViewAllAdaptor.onItemClickListener{
            override fun onItemClickDetail(position: Int)  {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveDetailFragment(position))
                    .commitNow()
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
                leave ->
            leaveArrayList.removeAll(leaveArrayList)
            leaveArrayList.addAll(leave)
            leaveRecordRecyclerView.adapter!!.notifyDataSetChanged()
        })


        binding.buttonCancelLeave.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.leave_cancel_dialog, null)
            val dialogBuilder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Confirmation Message")
            val alertDialog = dialogBuilder.show()
            val confirmYesButton = dialogView.findViewById<Button>(R.id.buttonYesCancelLeave)
            val confirmNoButton = dialogView.findViewById<Button>(R.id.buttonNoCancelLeave)

            confirmYesButton.setOnClickListener {
                alertDialog.dismiss()

                deleteRecord()

                confirmNoButton.setOnClickListener {
                    alertDialog.dismiss()
                }
            }

            confirmNoButton.setOnClickListener {
                alertDialog.dismiss()
            }

        }
    }

    internal fun deleteRecord(){

        for (i in leaveRecordAdapter.selectedCheckBoxList.indices){
            var noOfDays = leaveRecordAdapter.selectedCheckBoxList[i].leaveNoOfDays
            if (leaveRecordAdapter.selectedCheckBoxList[i].leaveType == "Annual Leave") {
                viewModel.updateAnnualLeaveBalance(noOfDays!!.toDouble())
            } else if (leaveRecordAdapter.selectedCheckBoxList[i].leaveType == "Sick Leave") {
                viewModel.updateSickLeaveBalance(noOfDays!!.toDouble())
            } else {
                viewModel.updateMaternityLeaveBalance(noOfDays!!.toDouble())
            }
            Log.d("firebase", leaveRecordAdapter.selectedCheckBoxList[i].leaveType)
            viewModel.delete(leaveRecordAdapter.selectedCheckBoxList[i])

        }
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
