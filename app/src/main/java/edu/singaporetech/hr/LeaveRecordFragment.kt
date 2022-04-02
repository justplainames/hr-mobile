package edu.singaporetech.hr

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
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
        leaveRecordAdapter.setOnItemClickListener(object:LeaveRecordViewAllAdaptor.onItemClickListener{
            override fun onItemClickDetail(position: Int)  {


                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveDetailFragment(position))
                    .commitNow()



            }

//            override fun onItemClickDetail(position: Int) {
//                requireActivity()
//                    .supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, LeaveDetailFragment())
//                    .commitNow()
//            }

        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
                leave ->
            leaveArrayList.removeAll(leaveArrayList)
            leaveArrayList.addAll(leave)
            leaveRecordRecyclerView.adapter!!.notifyDataSetChanged()
        })

        //viewModel.fetchItems()



        binding.editTextLeaveRecord.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = binding.editTextLeaveRecord.text.toString()


                viewModel.updateLeaveRecord(searchText)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })



        //binding.recyclerViewLeaveRecordViewAll.
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
            if (leaveRecordAdapter.selectedCheckBoxList[i].leaveType == "Annual Leave") {
                viewModel.updateAnnualLeaveBalance()
            } else if (leaveRecordAdapter.selectedCheckBoxList[i].leaveType == "Sick Leave") {
                viewModel.updateSickLeaveBalance()
            } else {
                viewModel.updateMaternityLeaveBalance()
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

private fun <E> ArrayList<E>.addAll(elements: ArrayList<Leave>?) {

}
