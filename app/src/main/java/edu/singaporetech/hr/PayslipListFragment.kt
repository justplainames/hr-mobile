package edu.singaporetech.hr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.singaporetech.hr.databinding.FragmentPayslipListBinding

class PayslipListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var adapter : PayslipAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipListBinding>(
            inflater,R.layout.fragment_payslip_list, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListObserver = Observer<ArrayList<Payslip>> { items->
            adapter=PayslipAdapter(items) // add items to adapter
            binding.payslipListRecyclerView.adapter=adapter
        }

        viewModel.payslip.observe(requireActivity(), payslipListObserver)

        binding.payslipListRecyclerView.layoutManager=LinearLayoutManager(activity)
        binding.payslipListRecyclerView.setHasFixedSize(true)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }

}