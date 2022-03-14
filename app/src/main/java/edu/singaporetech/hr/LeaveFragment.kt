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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.singaporetech.hr.databinding.FragmentLeaveBinding
import edu.singaporetech.hr.leave.LeaveRecordAdaptor
import edu.singaporetech.hr.leave.LeaveRecordItem


class LeaveFragment : Fragment() {

    private lateinit var leaveArrayList: ArrayList<Leave>
    private lateinit var binding: FragmentLeaveBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var leaveAdapter: LeaveRecordAdaptor
    private lateinit var leaveRecyclerView: RecyclerView
    private val viewModel: LeaveViewModel by viewModels()
    private var _leaveRecord = ArrayList<Leave>()

    private var _leaveType = ArrayList<LeaveType>()
    private var leave = Leave()
//    val viewModel = ViewModelProvider(this)[LeaveViewModel::class.java]
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leave, container, false
        )



        var text = viewModel.fetchLeaveTypes()
//        var textvieww = text.
        viewModel.leaveType.observe(viewLifecycleOwner, Observer {
                leaveTypes ->
            binding.textViewAnnualLeaveTotal.setText(leaveTypes[0].annualLeaveTotal.toString())
//            get(0).toString())
        })
//    binding.textViewAnnualLeaveTotal.setText(textvieww.toString())
//    _leaveType.get(0).annualLeaveTotal
//        viewModel.leave.observe(viewLifecycleOwner, Observer {
//                leave ->
//
//            binding.textViewAnnualLeaveTotal = _leaveTypes
//            leaveArrayList.removeAll(leaveArrayList)
//            leaveArrayList.addAll(leave)
//            leaveRecyclerView.adapter!!.notifyDataSetChanged()
//    })
//        binding.textViewAnnualLeaveTotal =

//        var annualLeaveTotal: Float? = viewModel.displaytextView()?.toFloat()?: 0.0f
//        binding.textViewAnnualLeaveTotal.text = annualLeaveTotal.toString()
        // Recycler view
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

        viewModel.fetchItems()


//        getLeaveData()

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

//    private fun getLeaveData() {
//        db = FirebaseFirestore.getInstance()
//        db.collection("leave")
//            .addSnapshotListener(object: EventListener<QuerySnapshot>{
//                override fun onEvent(
//                    value: QuerySnapshot?,
//                    error : FirebaseFirestoreException?
//                ){
//                    if (error != null){
//                        Log.e("firestore Error", error.message.toString())
//                    }
//
//                    for (dc:DocumentChange in value?.documentChanges!!){
//                        if(dc.type == DocumentChange.Type.ADDED){
//                            leaveArrayList.add(dc.document.toObject(Leave::class.java))
//                        }
//                    }
//
//                    leaveAdapter.notifyDataSetChanged()
//                }
//
//            })
//    }


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

//        viewModel = ViewModelProvider.of(this).get(LeaveViewModel::class.java)
//        viewModel.leave
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