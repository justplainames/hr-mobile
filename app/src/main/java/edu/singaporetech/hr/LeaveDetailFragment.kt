package edu.singaporetech.hr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.singaporetech.hr.databinding.FragmentLeaveDetailBinding
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem

private const val ARG_PARAM1 = "title"

class LeaveDetailFragment(private var position: Int) : Fragment() {

    private lateinit var viewModel: LeaveRecordViewModel
    private lateinit var adaptorDetail: LeaveDetailAdaptor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLeaveDetailBinding>(
            inflater,R.layout.fragment_leave_detail, container, false
        )

        viewModel = ViewModelProvider(requireActivity()).get(LeaveRecordViewModel::class.java)

        val leaveRecordObserver = Observer<ArrayList<LeaveRecordViewAllItem>> { leave ->
            adaptorDetail = LeaveDetailAdaptor(leave, position)
            binding.recyclerViewLeaveDetail.adapter = adaptorDetail
        }
        viewModel.leave.observe(requireActivity(), leaveRecordObserver)

        binding.recyclerViewLeaveDetail.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewLeaveDetail.setHasFixedSize(true)



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
                    .replace(R.id.fragmentContainerView, LeaveRecordFragment())
                    .commitNow()
            }
        })
    }




}