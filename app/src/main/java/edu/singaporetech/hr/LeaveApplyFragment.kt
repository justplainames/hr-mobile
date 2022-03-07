package edu.singaporetech.hr

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import edu.singaporetech.hr.databinding.FragmentLeaveApplyBinding

class LeaveApplyFragment : Fragment() {

    lateinit var binding: FragmentLeaveApplyBinding
    private lateinit var dataHelper:DBHelper
    var db:SQLiteDatabase?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLeaveApplyBinding>(
            inflater,
            R.layout.fragment_leave_apply, container, false)

        // Inflate the layout for this fragment
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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object:
//            OnBackPressedCallback(true){
//
//            override fun handleOnBackPressed() {
//                (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
//                requireActivity()
//                    .supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, LeaveFragment())
//                    .commitNow()
//            }
//        })
//        initialise(view)
//    }

//    fun initialise(v:View){
//        dataHelper = DBHelper(v.context)
//        binding.buttonSubmitLeave.setOnClickListener{
//            sendData(it)
//        }
//
//    }

//    private fun sendData(view: View?) {
//        if
//        val newLeave = Leave(
//            leaveType =
//        )
//
//    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Leave"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}